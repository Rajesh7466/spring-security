package org.example.service;

import java.util.List;

import org.example.entity.UserInformation;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

	@Autowired
	UserRepository userRepository;
	public List<UserInformation> findAllUser() {
		 
		return userRepository.findAll();
	}
	
	
}
