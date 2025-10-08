package org.example.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.example.dto.AdressDto;
import org.example.dto.ChangePasword_Dto;
import org.example.dto.User_Info_dto;
import org.example.dto.User_Login_Info;
import org.example.entity.UserAdress;
import org.example.entity.UserInformation;
import org.example.repository.AdressRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	AdressRepository addressRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
//	this is for signin logic 
	public  String userSignUp(User_Info_dto dto) {
		if(userRepository.findById(dto.getEmailId()).isPresent()) {
			logger.warn("This emaild is already existed ...");
			return "Emaild is already existed .. please try with new one ";
		}else {
			UserInformation userInfo=new UserInformation(dto.getEmailId(),bCryptPasswordEncoder.encode(dto.getPassword()),dto.getMobile(),dto.getUsername());
			userRepository.save(userInfo);
			return "User registrated sucessfully... With username :"+userInfo.getUsername();
		}
	}
//	this is for login logic
	public    String  userLogin(User_Login_Info dto) {
		UserInformation information= userRepository.findByEmailIdAndPassword(dto.getEmailId(), dto.getPassword());
		if(information!=null) {
			return "Login Sucesfulll.........."+information.getUsername();
			 
		}else {
			 return "user id is invalid ";
		}
 	}
//	this is for change password
	public String changePassword(ChangePasword_Dto dto, String id) {
		 Optional<UserInformation> info=userRepository.findById(id);
		 if(info.isPresent()) {
			 UserInformation userInformation=info.get();
			 userInformation.setPassword(bCryptPasswordEncoder.encode(dto.getNew_Password()));
//			 userInformation.setPassword(dto.getNew_Password());
			 userRepository.save(userInformation);
			 return "password chand sucessfully.."+userInformation.getUsername();
		 }else {
			 logger.warn("this is invaild your userid...");
			return "the id is incorrect.. please provide the valid emailId...";
		} 
	}
 public  String addAdress(AdressDto dto, String emailId) {
		 UserInformation user=userRepository.findByEmailId(emailId).orElseThrow(()->new RuntimeException("Email id is not found"));
		 
		 if(user!=null) {
			 UserAdress adress=new UserAdress();
			 adress.setHouseNo(dto.getHouseNo());
			 adress.setStreet(dto.getStreet());
			 adress.setCity(dto.getCity());
			 adress.setPostalCode(dto.getPostalCode());
			 adress.setCountry(dto.getCountry());
			 adress.setState(dto.getState());
			 
			 
			 adress.setUser(user);
			  addressRepository.save(adress);
			  return "User adress added sucessfull.. with your user id "+user.getEmailId();
		 }
		 else {
			 return "User id is not found ";
		 }
		
	}
  
//   delete the user from data base
 public String deleteById(String emailId) {
	 UserInformation user=userRepository.findById(emailId).orElseThrow(()-> new UsernameNotFoundException("User is not found "));
	 	if(user==null) {
	 		return "emailid is wrong please provide the valid emailid";
	 	}else {
 		 userRepository.deleteById(emailId);
 		 return "User Deleted sucessfully....";
 		}
	 }
	 

}
