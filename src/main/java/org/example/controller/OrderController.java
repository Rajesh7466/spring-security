package org.example.controller;

import java.util.List;

import org.example.dto.OrderRequestDto;
import org.example.dto.OrderResponseDto;
import org.example.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*", 
    allowedHeaders = {"Content-Type", "Authorization", "Accept", "Origin"},
    exposedHeaders = {"Authorization"},
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class OrderController {
	@Autowired
	OrderService orderService;
	private static final Logger logger=LoggerFactory.getLogger(OrderController.class);
	/*
	  * place a new order
	  * EndPoint: POST /order/place/{emailId}
	  * requestBody : {
	  * DeliveryAdress: "123 Street, city"
	  * "PaymentType": "cash on delivery",
	  * "Special Instruction":"optional"
	  * }
	  * secured: yes, jwt token is required
	  * */
	  
	@PostMapping("/place/{emailId}")
	public ResponseEntity<OrderResponseDto> placeOrder(@PathVariable String emailId, @RequestBody OrderRequestDto requestDto){
		System.out.println("placing in order for this email :"+emailId); 
		logger.info("Request is recived to place the order to user {} ",emailId);
		 try {
			OrderResponseDto response=orderService.placeOrder(emailId,requestDto);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch ( RuntimeException e) {
			// TODO: handle exception
			logger.error("Error placinfg in order ",e.getMessage());
			return new ResponseEntity< >(null,HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			  logger.error("Unexpected error placing order: {}", e.getMessage());
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 *get order history for a user
	 *Endpoint : /order/history/emailId
	 * secured : yes jwt verification is required
	 * */
	@GetMapping("/history/{emailId}")
	public ResponseEntity<List<OrderResponseDto>> getOrderHistory(@PathVariable String emailId){
		  logger.info("Request received to fetch order history for user: {}", emailId);
		  try {
			List<OrderResponseDto> response=orderService.getOrderHistory(emailId);
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error fetchin in order history : {}",emailId);
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	
}
