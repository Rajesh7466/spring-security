package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderResponseDto {
	  private long orderId;
	    private String userEmail;
	    private String userName;
	    private LocalDateTime orderDate;
	    private double totalAmount;
	    private String status;
	    private String deliveryAddress;
	    private String paymentType;
	    private String specialInstructions;
	    private List<OrderItemResponseDto> items;
}
