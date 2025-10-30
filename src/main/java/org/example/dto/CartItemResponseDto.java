package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartItemResponseDto {
	
	 private long cartItemId;
	    private long productId;
	    private String productName;
	    private String productImage;
	    private double productPrice;
	    private int quantity;
	    private double subtotal;
}
