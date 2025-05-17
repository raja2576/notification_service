package com.PepSales_2576.notificationservice.modal;


import java.io.Serializable;

public class NotificationRequest implements Serializable {
    private String userId;
    private String email; 
    private String type;
    private String subject;
    private String message;
    private String phone;    // for SMS
    public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	// Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

