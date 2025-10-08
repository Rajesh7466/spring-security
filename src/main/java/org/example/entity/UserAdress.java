package org.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data 
@Getter
@Setter
@Entity
@Table(name="User_adress")
public class UserAdress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String houseNo;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	@ManyToOne
	@JoinColumn(name = "user_id")
	 @JsonBackReference // CHANGE THIS LINE
	private UserInformation user;
}
