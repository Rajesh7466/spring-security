package org.example.dto;

import java.util.List;

import org.example.entity.UserAdress;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserLogin_Response {
	
	private String emailId;
    private String mobile;
    private String username;
    
    private List<UserAdress> adresses;
}
