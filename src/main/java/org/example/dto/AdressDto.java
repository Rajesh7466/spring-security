package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AdressDto {
	private String houseNo;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
}
