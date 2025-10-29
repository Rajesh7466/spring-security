package org.example.controller;

import org.example.dto.CartRequestDto;
import org.example.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class CartController {
	
	
	private static final Logger logger=LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	CartService cartService;
/*
 * Add to cart
 * EndPoint: post /cart/add/{emailId}
 * secured : jwt token is required
 * */
	@PostMapping("/add/{emailId}")
	public ResponseEntity<String> addToCart(@PathVariable String emailId,@RequestBody CartRequestDto requestDto){
		try {
			String response=cartService.addToCart(emailId,requestDto);
			if(response.contains("sucessfully")) {
				return new ResponseEntity<>(response,HttpStatus.OK); 
			}else {
				return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST); 
			}
		}catch (Exception e) {
			 logger.error("error adding in product : ",e.getMessage());
			 return new ResponseEntity<>("Error: "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
