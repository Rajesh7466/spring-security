package org.example.controller;

 

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.dto.AdminOrderDto;
import org.example.dto.StatusUpdateDto;
import org.example.entity.OrderEntity;
import org.example.entity.UserInformation;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

public class AdminController {
@Autowired
AdminService adminService;

@Autowired
OrderRepository orderRepository;
@Autowired
private UserRepository userRepository;

private static final Logger logger=LoggerFactory.getLogger(AdminController.class);

	@PostMapping("/public/find/")
	public List<UserInformation>  findAlluser(){
		
		return adminService.findAllUser();
	}
	
	/**
     * Get all orders for admin dashboard
     * Endpoint: GET /admin/orders/all
     * Returns: List of all orders from all users
     */
	@GetMapping("/public/order")
		public ResponseEntity<List<AdminOrderDto>> getAllordersforAdmin(){
		logger.info("Admin request to fetch all orders");
		try {
//			fetch all order from database
			List<OrderEntity> allOrders=orderRepository.findAll();
			if(allOrders.isEmpty()) {
				logger.info("No order from database");
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
			}
//			convert orderentity to adminorderdto
			List<AdminOrderDto> orderDtos=allOrders.stream()
					.map( order->{
						AdminOrderDto dto=new AdminOrderDto();
						dto.setOrderId(order.getId());
						dto.setUserName(order.getUser().getUsername());
						dto.setUserEmail(order.getUser().getEmailId());
						dto.setOrderDate(order.getOrderDate());
						dto.setTotalAmount(order.getTotalAmount());
						dto.setStatus(order.getStatus());
						dto.setDeliveryAddress(order.getDeliveryAdress());
						dto.setPaymentType(order.getPaymentType());
						dto.setItemCount(order.getOrderItems() !=null ?order.getOrderItems().size() : 0);
						
//						get product name  for this order
						if(order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
							String productnames=order.getOrderItems().stream()
									.map(item-> item.getProduct().getName())
									.collect(Collectors.joining(", "));
							dto.setProductNames(productnames);
						}else {
						dto.setProductNames("No items");	
						}	
						return dto;
					}).collect(Collectors.toList());
					 return new ResponseEntity< >(orderDtos,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	/**
     * Update order status
     * Endpoint: PUT /admin/orders/{orderId}/status
     * Body: { "status": "CONFIRMED" }
     */
	@PutMapping("/admin/order/{orderId}/status")
	public ResponseEntity<String> updatOrderStaus(
			@PathVariable long orderId,
			@RequestBody StatusUpdateDto statusDto){
		logger.info("Admin request to update order {} status to {}", orderId, statusDto.getStatus());
		try {
			OrderEntity order=orderRepository.findById(orderId)
					.orElseThrow(()->new RuntimeException("order not found with order ID: "+orderId));
//			update status
			order.setStatus(statusDto.getStatus());
			orderRepository.save(order);
			logger.info("order status is updated {} ",order,statusDto.getStatus());
			return new ResponseEntity< >("order status updated sucessfully ",HttpStatus.OK);
		}catch (RuntimeException e) {
			 logger.error("Order not found: {}", e.getMessage());
	            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);	
		} 
		catch (Exception e) {
			logger.error("Error updating order status: {}", e.getMessage());
            return new ResponseEntity<>("Error updating status", HttpStatus.INTERNAL_SERVER_ERROR);
        
          }
		}
	
	
	
	}

	
	

