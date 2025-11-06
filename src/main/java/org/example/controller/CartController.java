package org.example.controller;

import org.example.SpringSecurityApplication;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", 
allowedHeaders = {"Content-Type", "Authorization", "Accept", "Origin"},
exposedHeaders = {"Authorization"},
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class CartController {

    private final SpringSecurityApplication springSecurityApplication;
	
	
	private static final Logger logger=LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	CartService cartService;

    CartController(SpringSecurityApplication springSecurityApplication) {
        this.springSecurityApplication = springSecurityApplication;
    }
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
	public ResponseEntity<String>removeFromCart(@PathVariable long cartItemId,@PathVariable String emailId){
		 logger.info("Request received to remove cart item: {} for user: {}", cartItemId, emailId);
		 try {
			String response=cartService.removeFromCart(cartItemId,emailId);
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error removing cart item: {}", e.getMessage());
			return new ResponseEntity<>("Error : "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
    /**
     * Update cart item quantity
     * Endpoint: PUT /cart/update/{cartItemId}/{emailId}
     * Request Body: { "productId": 1, "quantity": 5 }
     * Secured: Yes (JWT required)
     */
	@PutMapping("update/{cartItemId}/{emailId}")
	public ResponseEntity<CartResponseDto> updateCartItemQuantity(@PathVariable long cartItemId,
			@PathVariable String emailId,@RequestBody CartRequestDto requestDto){
		logger.info("Request received to update cart item: {} quantity to: {} for user: {}", 
                cartItemId, requestDto.getQuantity(), emailId);
		try {
			CartResponseDto response=cartService.updateCartItemQuantity(cartItemId,requestDto.getQuantity(),emailId);
			return new ResponseEntity< >(response,HttpStatus.OK);
		} catch (Exception e) {
			  logger.error("Error updating cart item quantity: {}", e.getMessage());
			  return new ResponseEntity< >(null,HttpStatus.BAD_REQUEST);
		}
	}
	
    /**
     * Clear entire cart
     * Endpoint: DELETE /cart/clear/{emailId}
     * Secured: Yes (JWT required)
     */
     @DeleteMapping("/clear/{emailId}")
     public ResponseEntity<String> clearCart(@PathVariable String emailId){
    	   		logger.info("request recived to clear cart for a user : {}",emailId);
    	   		try {
					return null;
				} catch (Exception e) {
					// TODO: handle exception
					return null;                     
				}
     }
	
}
