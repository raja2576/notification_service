package com.PepSales_2576.notificationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PepSales_2576.notificationservice.modal.BulkNotificationRequest;
import com.PepSales_2576.notificationservice.modal.NotificationRequest;
import com.PepSales_2576.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        notificationService.processNotification(request);
        return ResponseEntity.ok("Notification queued");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }
    
    @PostMapping("/bulk")
    public ResponseEntity<String> sendBulkNotification(@RequestBody BulkNotificationRequest request) {
        try {
            notificationService.processBulkNotification(request);
            return ResponseEntity.ok("Bulk notification sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send bulk notification: " + e.getMessage());
        }
    }
}

