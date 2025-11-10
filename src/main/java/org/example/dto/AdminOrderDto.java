package org.example.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class AdminOrderDto {
	private long orderId;
    private String userName;
    private String userEmail;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String status;
    private String deliveryAddress;
    private String paymentType;
    private int itemCount;
    private String productNames;
}
