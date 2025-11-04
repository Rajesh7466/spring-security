package org.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.dto.OrderItemResponseDto;
import org.example.dto.OrderRequestDto;
import org.example.dto.OrderResponseDto;
import org.example.entity.CartEntity;
import org.example.entity.CartItem;
import org.example.entity.OrderEntity;
import org.example.entity.OrderItems;
import org.example.entity.ProductEntity;
import org.example.entity.UserInformation;
import org.example.repository.CartItemRepository;
import org.example.repository.CartRepository;
import org.example.repository.OrderItemsRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	CartItemRepository cartItemRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	CartService cartService;
	@Autowired
	OrderItemsRepository orderItemsRepository;
	private static final Logger logger=LoggerFactory.getLogger(OrderService.class);
	@Transactional
	public OrderResponseDto placeOrder(String emailId, OrderRequestDto requestDto) {
		 logger.info("Placing order for user : {}",emailId);
//	1.	 find User
		 UserInformation user=userRepository.findById(emailId).orElseThrow(()-> new UsernameNotFoundException("User is not found"));
		 
//	2.	 find users from cart
		 CartEntity cart=cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("Cart not found for user"));
//	3.	 get item form cart 
		 List<CartItem> cartItems=cartItemRepository.findByCart(cart);
		 
//	4.	 validity stock for all product
		 for(CartItem cartItem :cartItems) {
			 ProductEntity product=cartItem.getProduct();
			 if(product.getStockQuantity()<cartItem.getQuantity()) {
				 throw new RuntimeException("Insufficient stock for product: "+
			 product.getName()+". Available: "+product.getStockQuantity()+", Required: " + cartItem.getQuantity());
			 }
		 }
		 
		 // 5. Calculate total amount
		 double totalAmount=0.0;
		 for(CartItem cartItem: cartItems) {
			totalAmount+= (cartItem.getQuantity() * cartItem.getProduct().getPrice());
		 }
//	5.	 create order entity
		 OrderEntity order=new OrderEntity();
		 order.setUser(user);
		 order.setTotalAmount(totalAmount);
		 order.setPaymentType(requestDto.getPaymentType() !=null ? requestDto.getPaymentType() : "Cash on delivery");
		 order.setStatus("pending");
		 order.setOrderDate( LocalDateTime.now());
		 order.setDeliveryAdress(requestDto.getDeliveryAddress());
		 
//	6.	 save order to generate id 
		 order=orderRepository.save(order);
		  logger.info("Order created with ID: {}", order.getId());
		  
//	7.	  create order items
		  List<OrderItems> orderItemList=new ArrayList<>();
		  for(CartItem cartItem : cartItems) {
			  OrderItems orderItems=new OrderItems();
			  orderItems.setOrder(order);
			  orderItems.setProduct(cartItem.getProduct());
			  orderItems.setQuantity(cartItem.getQuantity());
			  orderItems.setPrice(cartItem.getProduct().getPrice());
			  orderItems=orderItemsRepository.save(orderItems);
			  orderItemList.add(orderItems);
		  }
		  order.setOrderItems(orderItemList);
		  
//	8.	  Update product from stock 
		  for(CartItem cartItem : cartItems) {
			  ProductEntity product=cartItem.getProduct();
			  product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
			  productRepository.save(product);
			  logger.info("Updated stock for product: {}. New stock: {}", product.getName(), product.getStockQuantity());
		  }
//		  9.  clears users from cart
		  cartService.clearCart(emailId);
		  logger.info("Cart cleared for user: {}", emailId);
		  
		  
		  // 10. Prepare response
		  OrderResponseDto response=mapOrderToResponseDto(order);
		  logger.info("Order placed successfully with ID: {}", order.getId());
		return  response;
	}
	
	public List<OrderResponseDto> getOrderHistory(String emailId){
		  logger.info("Fetching order history for user: {}", emailId);
		  UserInformation user = userRepository.findById(emailId)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		  List<OrderEntity> orders=orderRepository.findByUserOrderByOrderDateDesc(user);
		  List<OrderResponseDto> responseDtos=new ArrayList<>();
		  for(OrderEntity order : orders) {
			  responseDtos.add(mapOrderToResponseDto(order));
		  }
		return  responseDtos;
	}

	
	// Helper method to map Order Entity to Response DTO
	private OrderResponseDto mapOrderToResponseDto(OrderEntity order) {
		OrderResponseDto response=new OrderResponseDto();
		response.setOrderId(order.getId());
		response.setUserEmail(order.getUser().getEmailId());
		response.setUserName(order.getUser().getUsername());
		response.setOrderDate(order.getOrderDate());
		response.setTotalAmount(order.getTotalAmount());
		response.setStatus(order.getStatus());
		response.setDeliveryAddress(order.getDeliveryAdress());
		response.setPaymentType(order.getPaymentType());
		
		List<OrderItemResponseDto> itemDtos=new ArrayList<>();
		for(OrderItems orderItems : order.getOrderItems()) {
			OrderItemResponseDto itemDto=new OrderItemResponseDto();
			itemDto.setOrderItemId(orderItems.getId());
			itemDto.setProductId(orderItems.getProduct().getId());
			itemDto.setProductName(orderItems.getProduct().getName());
			itemDto.setProductImage(orderItems.getProduct().getImageUrl());
			itemDto.setQuantity(orderItems.getQuantity());
			itemDto.setPrice(orderItems.getPrice());
			itemDto.setSubtotal(orderItems.getPrice() * orderItems.getQuantity());
			
			itemDtos.add(itemDto);
		}
		response.setItems(itemDtos);
		return response;
	}
	
}
