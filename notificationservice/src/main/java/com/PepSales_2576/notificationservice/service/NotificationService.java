package com.PepSales_2576.notificationservice.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.PepSales_2576.notificationservice.modal.BulkNotificationRequest;
import com.PepSales_2576.notificationservice.modal.NotificationRequest;

public interface NotificationService {
    void processNotification(NotificationRequest request);
    List<String> getNotificationsForUser(String userId);
    void processBulkNotification(BulkNotificationRequest request);

}

