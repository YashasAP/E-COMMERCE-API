package com.mystore.dto;

import com.mystore.enums.UserRole;

//import lombok.Data;
//
//@Data
public class UserRegistrationRequest {
    private String username;
    private String password;
    private UserRole role;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
    
    
    
}