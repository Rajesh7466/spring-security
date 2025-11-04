package org.example.service;

 

import org.example.dto.ProductDto;
import org.example.entity.ProductEntity;
import org.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	
	private static final Logger logger=LoggerFactory.getLogger(ProductService.class);
	
	@Autowired
	 ProductRepository productRepository;
	
//	Add new product
	public   ProductEntity addProduct(ProductDto productDto) {
		logger.info("Adding new product: {}", productDto.getName());
		
		ProductEntity product=new ProductEntity();
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setStockQuantity(productDto.getStockQuantity());
		product.setImageUrl(productDto.getImageUrl());
		
		ProductEntity  savedProduct=productRepository.save(product);
		 
		return savedProduct;
	}
	
 
	 
}
