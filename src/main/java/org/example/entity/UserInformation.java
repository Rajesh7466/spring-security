package org.example.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
@Entity
@Table(name="User_Information")
public class UserInformation implements UserDetails {
	@Id
	private String emailId;
	private String password;
	private String mobile;
	private String   fullName;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	 @JsonManagedReference // <--- CORRECT: This tells Jackson to include 'adresses' when UserInformation is serialized.
	 private List<UserAdress> adress;
	
	public UserInformation() {
		
	}
	public UserInformation(String emailId, String password, String mobile, String username) {
		super();
		this.emailId = emailId;
		this.password = password;
		this.mobile = mobile;
		this.fullName = username;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	
	 
	 @Override 
	 public String getUsername() { 
	  return emailId; 
	 } 
}
