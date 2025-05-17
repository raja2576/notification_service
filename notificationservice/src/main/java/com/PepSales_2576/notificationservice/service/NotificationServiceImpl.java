package com.PepSales_2576.notificationservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;

import com.PepSales_2576.notificationservice.config.EnvConfig;
import com.PepSales_2576.notificationservice.config.SmsSender;
import com.PepSales_2576.notificationservice.modal.BulkNotificationRequest;
import com.PepSales_2576.notificationservice.modal.Notification;
import com.PepSales_2576.notificationservice.modal.NotificationRequest;
import com.PepSales_2576.notificationservice.modal.User;
import com.PepSales_2576.notificationservice.queue.QueueProducer;
import com.PepSales_2576.notificationservice.repo.NotificationRepository;
import com.PepSales_2576.notificationservice.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SmsSender smsSender;

    private final String accountSid = EnvConfig.getEnv("TWILIO_ACCOUNT_SID");
    private final String authToken = EnvConfig.getEnv("TWILIO_AUTH_TOKEN");
    private final String fromPhoneNumber = EnvConfig.getEnv("TWILIO_PHONE_NUMBER");

    
 
    
    @Override
    public void processNotification(NotificationRequest request) {
        // Normalize and validate phone number if provided
        String rawNumber = request.getPhone();
        String toNumber = null;
        
        System.out.println(accountSid);
        

        if (rawNumber != null && !rawNumber.isBlank()) {
            // Remove all characters except digits and plus
            toNumber = rawNumber.replaceAll("[^\\d+]", "");

            // Add country code if missing
            if (!toNumber.startsWith("+")) {
                toNumber = "+91" + toNumber; // Change default country code as needed
            }

            // Validate E.164 phone number format: + followed by 10-15 digits
            if (!toNumber.matches("\\+\\d{10,15}")) {
                throw new IllegalArgumentException("Invalid phone number format: " + toNumber);
            }
        }

        // Validate presence of either email or phone
        if ((request.getEmail() == null || request.getEmail().isBlank()) &&
            (toNumber == null || toNumber.isBlank())) {
            throw new IllegalArgumentException("Either email or phone number must be provided.");
        }

        final String nbr = toNumber;  // Use normalized number going forward

        // Find or create user
        User user = userRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUserId(request.getUserId());
                    newUser.setEmail(request.getEmail());
                    newUser.setPhone(nbr);
                    return userRepository.save(newUser);
                });

        // Save in-app notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(request.getType());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);

        // Send notification based on type
        switch (request.getType().toLowerCase()) {
            case "email":
                if (user.getEmail() == null || user.getEmail().isBlank()) {
                    throw new IllegalArgumentException("User has no registered email.");
                }
                emailSender.sendEmail(user.getEmail(), request.getSubject(), request.getMessage());
                break;

            case "sms":
                if (nbr == null || nbr.isBlank()) {
                    throw new IllegalArgumentException("User has no registered phone number.");
                }
                System.out.println("Sending SMS to normalized phone number: " + nbr);
                smsSender.sendSms(nbr, request.getMessage());
                break;

            case "inapp":
                // No external send needed, notification saved above
                break;

            default:
                throw new IllegalArgumentException("Unsupported notification type: " + request.getType());
        }
    }

    @Override
    public List<String> getNotificationsForUser(String userId) {
        // Find the user by userId
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Fetch notifications for that user
        List<Notification> notifications = notificationRepository.findByUser(user);

        // Map Notification objects to their message strings (or any other info you want)
        return notifications.stream()
                .map(Notification::getMessage)
                .toList();
    }

	@Override
	public void processBulkNotification(BulkNotificationRequest request) {
	    if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
	        throw new IllegalArgumentException("User IDs list cannot be empty.");
	    }
	    if (request.getType() == null || request.getType().isBlank()) {
	        throw new IllegalArgumentException("Notification type must be specified.");
	    }

	    for (String userId : request.getUserIds()) {
	        // Fetch user
	        User user = userRepository.findByUserId(userId)
	                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

	        // Save in-app notification for each user (optional)
	        Notification notification = new Notification();
	        notification.setUser(user);
	        notification.setType(request.getType());
	        notification.setSubject(request.getSubject());
	        notification.setMessage(request.getMessage());
	        notification.setTimestamp(LocalDateTime.now());
	        notificationRepository.save(notification);

	        // Send notification according to type
	        switch (request.getType().toLowerCase()) {
	            case "email":
	                if (user.getEmail() == null || user.getEmail().isBlank()) {
	                    System.out.println("Skipping email for user " + userId + ": no registered email.");
	                    continue;
	                }
	                emailSender.sendEmail(user.getEmail(), request.getSubject(), request.getMessage());
	                break;

	            case "sms":
	                String phone = user.getPhone();
	                if (phone == null || phone.isBlank()) {
	                    System.out.println("Skipping SMS for user " + userId + ": no registered phone number.");
	                    continue;
	                }
	                smsSender.sendSms(phone, request.getMessage());
	                break;

	            default:
	                throw new IllegalArgumentException("Unsupported notification type: " + request.getType());
	        }
	    }
	}

}
