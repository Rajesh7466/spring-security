package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderItemResponseDto {
	   private long orderItemId;
	    private long productId;
	    private String productName;
	    private String productImage;
	    private int quantity;
	    private double price;
	    private double subtotal;
}
