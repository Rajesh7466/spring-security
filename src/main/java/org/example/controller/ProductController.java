package org.example.controller;

import java.util.List;

import org.example.entity.ProductEntity;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ProductController {
	@Autowired
	ProductService productService;
	
	@GetMapping
	public List<ProductEntity> getAllProducts(){
		return productService.getAllItems();
	}
}
