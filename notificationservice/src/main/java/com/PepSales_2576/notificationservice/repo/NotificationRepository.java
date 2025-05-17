package com.PepSales_2576.notificationservice.repo;

import com.PepSales_2576.notificationservice.modal.Notification;
import com.PepSales_2576.notificationservice.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Find notifications by User
    List<Notification> findByUser(User user);
    List<Notification> findByUser_UserId(String userId);
}

