package org.example.service;

import java.util.Optional;

import org.example.dto.CartRequestDto;
import org.example.entity.CartEntity;
import org.example.entity.CartItem;
import org.example.entity.ProductEntity;
import org.example.entity.UserInformation;
import org.example.repository.CartItemRepository;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CartService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	CartItemRepository cartItemRepository;
	
	private static final Logger logger=LoggerFactory.getLogger(CartService.class);

	public   String addToCart(String emailId, CartRequestDto requestDto) {
		logger.info("Adding product to cart for user: {}", emailId);
	
//			find user 
		UserInformation user=userRepository.findById(emailId)
				.orElseThrow(()->new UsernameNotFoundException("User is not found"));
//		find product
		ProductEntity product=productRepository.findById(requestDto.getProductId())
				.orElseThrow(()-> new RuntimeException("User Not found"));
//		check stock availability or not 
		if(product.getStockQuantity()<requestDto.getQuantity()) {
			return "insufficient stock : "+product.getName();
		}
//		find or create cart 
		CartEntity cart=cartRepository.findByUser(user).orElseGet(()->{
			CartEntity newCart=new CartEntity();
			newCart.setUser(user);
			return cartRepository.save(newCart);
		});
//		check if product already in cart
		Optional<CartItem> existingCartItem=cartItemRepository.findByCartAndProduct(cart,product);
		if(existingCartItem.isPresent()) {
//			Update Quantity
			CartItem cartItem=existingCartItem.get();
			int newQuantity=cartItem.getQuantity()+requestDto.getQuantity();
			if(product.getStockQuantity()<newQuantity) {
				return "Cannot add more. insuffcient stock in product : "+product.getName();
			}
			cartItem.setQuantity(newQuantity);
			cartItemRepository.save(cartItem);
			logger.info("Added new item to cart :{}" +product.getName());
		}
		else {
//			create new cart item 
			CartItem cartItem=new CartItem();
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(requestDto.getQuantity());
			cartItemRepository.save(cartItem);
			logger.info("Added new item to cart :{}",product.getName());
		}
		return  "product added to cart sucessfully";
	}

	public   void clearCart(String emailId) {
		logger.info("Clearing cart for user: {}", emailId);
		   UserInformation user = userRepository.findById(emailId)
	              .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		   Optional<CartEntity> cartOpt=cartRepository.findByUser(user);
		   if(cartOpt.isPresent()) {
			   CartEntity cart=cartOpt.get();
			   cartItemRepository.deleteByCart(cart);
			   logger.info("Cart cleared successfully for user: {}", emailId);
		   }
		
	}
	
	
}
