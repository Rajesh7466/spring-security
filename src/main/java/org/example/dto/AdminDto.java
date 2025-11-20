package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AdminDto {
	
	private String emailId;
	private String password;
}
