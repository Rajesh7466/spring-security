package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartRequestDto {
	
	private long productId;
	private int quantity;
}
