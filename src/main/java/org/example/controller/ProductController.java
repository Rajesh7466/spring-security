package org.example.controller;

import java.util.List;

import org.example.entity.ProductEntity;
import org.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/product")
public class ProductController {

	private static final Logger logger=LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductRepository productRepository;
	
	@GetMapping
	public ResponseEntity<List<ProductEntity>> getAllProduct(){
		logger.info("Request recived to fetch all product");
		try {
			List<ProductEntity> product=productRepository.findAll();
			return new ResponseEntity<>(product,HttpStatus.OK);
		}catch (Exception e) {
			 return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
/*
 * get product by id
 * Endpoint: get /public/products/{id}
 * public endpoint (No authentication is required)
 * */
	/*	@GetMapping("/{id}")
	public ResponseEntity<ProductEntity> getProductId(@PathVariable long id){
		logger.info("Request recived to fetch product with id :{}", id);
		try {
			ProductEntity product=ProductRepository.findById(id).orElseThrow();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}* */
	
}
