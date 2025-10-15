package org.example.service;

import org.example.entity.CartEntity;
import org.example.entity.UserInformation;
import org.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CartService {
	
	@Autowired
	CartRepository cartRepository;
	
	public static CartEntity addToCart(UserInformation user, Long productLid, int quantity) {
		 
		return null;
	}

	public CartEntity getUserByCart(UserInformation user) {
		// TODO Auto-generated method stub
		return null;
	}

}
