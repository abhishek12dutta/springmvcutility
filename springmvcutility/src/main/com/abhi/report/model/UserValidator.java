package com.abhi.report.model;

public class UserValidator {
	
	boolean isUsernameAvailable;
	String message;
	
	public boolean isUsernameAvailable() {
		return isUsernameAvailable;
	}
	public void setUsernameAvailable(boolean isUsernameAvailable) {
		this.isUsernameAvailable = isUsernameAvailable;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
