package org.example.controller;


import org.example.dto.AdressDto;
import org.example.dto.ChangePasword_Dto;
import org.example.dto.UserLogin_Response;
import org.example.dto.User_Info_dto;
import org.example.dto.User_Login_Info;
import org.example.repository.UserRepository;
import org.example.security.JwtUtill;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
//@CrossOrigin("*")
@CrossOrigin(origins = "*", 
allowedHeaders = {"Content-Type", "Authorization", "Accept", "Origin"},
exposedHeaders = {"Authorization"},
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class UserController {
	
	private static final Logger logger=LoggerFactory.getLogger("UserController.class");
	@Autowired
	UserRepository repository;
	@Autowired
	JwtUtill jwtUtill;
	@Autowired
	UserService userService;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/public/hello")
	public String hello() {
		return "hello";
	}
	
	
	@PostMapping("/public/user/register")
	public String userSignUp(@RequestBody User_Info_dto dto) {
		logger.info("User data is recived ... for registration "+ dto.getEmailId());
		System.out.println("data is recived : "+dto.getEmailId());
		
		return userService.userSignUp(dto);
	}
	
	@PostMapping("/public/login")
	public  ResponseEntity<UserLogin_Response> userLogin(@RequestBody User_Login_Info dto) {
		System.out.println("email id is "+dto.getEmailId());
//		validation of creadtionals:user and password combination
	//	String responseString=null;
		UsernamePasswordAuthenticationToken credentials=new UsernamePasswordAuthenticationToken(dto.getEmailId(), dto.getPassword());
		try {
			 authenticationManager.authenticate(credentials);
		System.out.println();
		
		}catch ( BadCredentialsException e) {
			System.err.println("UserController : Bad CredentialsEcception");
			 String response="'invalid Creadtionals";
//			 return new ResponseEntity<String>(response,HttpStatusCode.valueOf(401));
			 return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
			  
		}
		
		String token=jwtUtill.createToken(dto.getEmailId());
		org.springframework.http.HttpHeaders responseHeaders=new org.springframework.http.HttpHeaders();
		responseHeaders.add("Authorization",token);
		
		
		UserLogin_Response response=userService.userLogin(dto);
	//	return new ResponseEntity<String>("Welcome to home : "+dto.getEmailId(),responseHeaders,HttpStatus.valueOf( 200));
	 return new ResponseEntity<>(response,responseHeaders,HttpStatus.OK);
	}
	
	@PostMapping("/user/change/password/{emailId}")
	public String changePassword(@RequestBody ChangePasword_Dto dto,@PathVariable("emailId") String emailId) {
		System.out.println("new password is :"+dto.getNew_Password());
		return userService.changePassword(dto,emailId);
	}
	
	
	
 @PostMapping("/adress/{emailId}")
 @CrossOrigin(origins = "http://127.0.0.1:5501", // Be specific for production
 allowedHeaders = {"Content-Type", "Authorization", "Accept", "Origin"},
 exposedHeaders = {"Authorization"}, // Only if you are reading Authorization from this specific response
 methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
 public   ResponseEntity<AdressDto>  addAdress(@RequestBody AdressDto dto, @PathVariable ("emailId") String emailId){
	 logger.info("user Aress data is reicved ..."+ emailId);
	 System.out.println("Adress data is recived : "+ dto.getCity());
	AdressDto responseAdressDto= userService.addAdress(dto, emailId);
	if(responseAdressDto!=null) {
		return new ResponseEntity<>(responseAdressDto, HttpStatus.OK);
	}else {
		return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
	}
	   
 }
 
 
// 	delete user by id 
 @DeleteMapping("/delete/user/{emailId}")
 public ResponseEntity<String> deleteByUser(@PathVariable String emailId){
	 String response=userService.deleteById(emailId);
	 if(response.startsWith("User Deleted ")) {
		 return ResponseEntity.ok(response);
	 }else {
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	 }
 }
 
} 
