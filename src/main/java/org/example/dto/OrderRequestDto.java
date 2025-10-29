package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class OrderRequestDto {
	
	  private String deliveryAddress;
	  private String paymentType; // Default: "Cash on Delivery"
	  private String specialInstructions;
}
