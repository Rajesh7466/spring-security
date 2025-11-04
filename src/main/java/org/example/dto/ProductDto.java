package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductDto {
	private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String imageUrl;
}
