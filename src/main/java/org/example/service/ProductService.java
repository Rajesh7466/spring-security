package org.example.service;

import java.util.List;

import org.example.entity.ProductEntity;
import org.example.repository.ProductRepoitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	
	@Autowired
	ProductRepoitory productRepoitory;
	
	public List<ProductEntity> getAllItems() {
		// TODO Auto-generated method stub
		return productRepoitory.findAll() ;
	}

}
