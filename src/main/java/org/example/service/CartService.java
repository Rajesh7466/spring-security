package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.dto.CartItemResponseDto;
import org.example.dto.CartRequestDto;
import org.example.dto.CartResponseDto;
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
import org.springframework.transaction.annotation.Transactional;

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

	public CartResponseDto getCartItems(String emailId) {
		logger.info("Fetching cart items for user: {}", emailId);
		UserInformation user=userRepository.findById(emailId).orElseThrow(()-> new UsernameNotFoundException("User is not fond"));
		Optional<CartEntity> cartOpt=cartRepository.findByUser(user);
		if(cartOpt.isEmpty()) {
			CartResponseDto response=new CartResponseDto();
			response.setUserEmail(emailId);
			response.setItems(new ArrayList<>());
			response.setTotalAmount(0.0);
			response.setTotalItems(0);
			return response;
		}
		CartEntity cart=cartOpt.get();
		List<CartItem> cartItems=cartItemRepository.findByCart(cart);
		CartResponseDto response=new CartResponseDto();
		response.setCartId(cart.getId());
		response.setUserEmail(emailId);
		
		List<CartItemResponseDto> itemsDtos=new ArrayList<>();
		double totalAmount=0.0;
		int totalItems=0;
		
		for(CartItem item :cartItems) {
			CartItemResponseDto itemDto=new CartItemResponseDto();
			itemDto.setCartItemId(item.getId());
			itemDto.setProductId(item.getProduct().getId());
			itemDto.setProductName(item.getProduct().getName());
			itemDto.setProductImage(item.getProduct().getImageUrl());
			itemDto.setProductPrice(item.getProduct().getPrice());
			itemDto.setQuantity(item.getQuantity());
			itemDto.setSubtotal(item.getProduct().getPrice() * item.getQuantity());
			
			itemsDtos.add(itemDto);
			totalAmount+=itemDto.getSubtotal();
			totalItems+=item.getQuantity();
		}
		response.setItems(itemsDtos);
		response.setTotalAmount(totalAmount);
		response.setTotalItems(totalItems);
		return response;
	}

	@Transactional
	public String removeFromCart(long cartItemId, String emailId) {
		 logger.info("Removing cart item: {} for user: {}", cartItemId, emailId);
		 UserInformation user=userRepository.findById(emailId)
				 .orElseThrow(()->new UsernameNotFoundException("User is not found"));
		 CartItem cartItem=cartItemRepository.findById(cartItemId)
				 .orElseThrow(()->new RuntimeException("cart is not found "));
				 
//				  verify the cart the cart item belong to user or not 
		 if(!cartItem.getCart().getUser().getEmailId().equals(emailId)){
			 throw new RuntimeException("Unauthorized access to cart item");
		 }
		 cartItemRepository.delete(cartItem);
		 return "Item removed from cart sucessfully";
	}

	@Transactional
	public CartResponseDto updateCartItemQuantity(long cartItemId, int quantity, String emailId) {
		   logger.info("Updating cart item: {} quantity to: {} for user: {}", cartItemId, quantity, emailId);
		   UserInformation user = userRepository.findById(emailId)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	        CartItem cartItem = cartItemRepository.findById(cartItemId)
	                .orElseThrow(() -> new RuntimeException("Cart item not found"));
	        
	     // Verify the cart item belongs to the user
	        if(!cartItem.getCart().getUser().getEmailId().equals(emailId)) {
	        	  throw new RuntimeException("cart item is not found");
	        }
//	        check stock availability
	        if(cartItem.getProduct().getStockQuantity() < quantity) {
	        	 throw new RuntimeException("Insufficient stock for product: "+ cartItem.getProduct().getName() );
	        }
	        if(quantity<=0){
//	        	  remove item if quantity is 0 or negative 
	        	cartItemRepository.delete(cartItem);
	        	 logger.info("Cart item removed due to zero quantity");
	        }else {
	        	  cartItem.setQuantity(quantity);
	        	  cartItemRepository.save(cartItem);
	        	  logger.info("Cart item quantity updated successfully");
	        }
//	        return updated cart
	        return getCartItems(emailId);
	}
	
	 
	
}
