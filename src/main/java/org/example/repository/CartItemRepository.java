package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.entity.CartEntity;
import org.example.entity.CartItem;
import org.example.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	 Optional<CartItem> findByCartAndProduct(CartEntity cart,ProductEntity product);

	List<CartItem> findByCart(CartEntity cart);

	void deleteByCart(CartEntity cart);

	 
	}


