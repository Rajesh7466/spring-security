package org.example.entity;

 

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
@Entity
@Table(name="orders")
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="user_email")
	private UserInformation user;
	
	private double totalAmount;
	private String PaymentType;
	private String status;
	private LocalDateTime orderDate;
	
	private String deliveryAdress;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItems> orderItems;
	
	 
	
}
