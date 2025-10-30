package org.example.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartResponseDto {
	 private long cartId;
	    private String userEmail;
	    private List<CartItemResponseDto> items;
	    private double totalAmount;
	    private int totalItems;
}
