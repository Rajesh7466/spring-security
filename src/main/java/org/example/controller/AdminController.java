package org.example.controller;

 

import java.util.List;

import org.example.entity.UserInformation;
import org.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AdminController {
@Autowired
AdminService adminService;

	@PostMapping("/public/find/")
	public List<UserInformation>  findAlluser(){
		
		return adminService.findAllUser();
	}
}
