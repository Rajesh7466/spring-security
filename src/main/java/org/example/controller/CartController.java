package org.example.controller;

import org.example.dto.CartRequestDto;
import org.example.dto.CartResponseDto;
import org.example.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@CrossOrigin(origins = "*", 
allowedHeaders = {"Content-Type", "Authorization", "Accept", "Origin"},
exposedHeaders = {"Authorization"},
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
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
	
	/*
	 * get all cart item from a user
	 * Endpoint: GET /cart/{emailId}
	 * Secured: Yes (JWT required)
	 * */
	
	@GetMapping("/{emailId}")
	public ResponseEntity<CartResponseDto> getCartItems(@PathVariable String emailId){
		 logger.info("Request received to fetch cart items for user: {}", emailId);
		 try {
			CartResponseDto response=cartService.getCartItems(emailId);
			return new ResponseEntity<>(response,HttpStatus.OK);
			} catch (Exception e) {
				 logger.error("Error fetching cart items: {}", e.getMessage());
		            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*Remove item from cart
	 * Endpoint: DELETE /cart/item/{cartItemId}/{emailId}
	 * Secured: Yes (JWT required)
	 * */
	@DeleteMapping("/cart/{cartItemId}/{emailId}")
	public ResponseEntity<String>removeFromCart(@PathVariable long CartItemId,@PathVariable String emailId){
		return null;
	}
	
}
