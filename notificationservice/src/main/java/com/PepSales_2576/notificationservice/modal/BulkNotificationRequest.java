package com.PepSales_2576.notificationservice.modal;

import java.util.List;

public class BulkNotificationRequest {
    private List<String> userIds;
    private String type;      // "sms" or "email"
    private String subject;
    private String message;

    // Getters and Setters
    public List<String> getUserIds() {
        return userIds;
    }
    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
