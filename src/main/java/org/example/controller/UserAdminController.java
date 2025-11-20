package org.example.controller;

import java.util.List;
import java.util.Optional;

import org.example.dto.AdminDto;
import org.example.dto.AdminOrderDto;
import org.example.entity.AdminEntity;
import org.example.repository.UserAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class UserAdminController {
	
	private static final Logger logger=LoggerFactory.getLogger(UserAdminController.class);
	@Autowired
	UserAdminRepository userAdminRepo;
	
/*for admin login
 * endpoint: /admin/login
 * */
	@PostMapping("/login")
	public ResponseEntity<String> adminLogin(@RequestBody AdminDto dto){
		logger.info("for admin login data is recived in controller, {}",dto.getEmailId());
		try {
			 Optional<AdminEntity> response=userAdminRepo.findById(dto.getEmailId());
			if (response.isEmpty()) {
				return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
			}else {
				return new ResponseEntity<>("user Login sucessfully",HttpStatus.OK);
			}
		} catch (Exception e) {
			 return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
