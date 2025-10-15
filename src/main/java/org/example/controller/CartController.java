package org.example.controller;

import org.example.entity.CartEntity;
import org.example.entity.UserInformation;
import org.example.service.CartService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CartController {

    private final UserService userService;

	@Autowired
	 CartService cartService;


    CartController(UserService userService) {
        this.userService = userService;
    }
	 
	
	@PostMapping("/add")
	public CartEntity addToCart(@RequestParam String emailId, @RequestParam Long productLid, @RequestParam int quantity) {
		
		UserInformation user=new UserInformation();
		user.setEmailId(emailId);
		return CartService.addToCart(user, productLid,quantity);
		 
	}
	
	@GetMapping("/getCart")
	public CartEntity getCart(@RequestParam String emailId) {
		UserInformation user=new UserInformation();
		user.setEmailId(emailId);
		return cartService.getUserByCart(user);
	}
	
}
