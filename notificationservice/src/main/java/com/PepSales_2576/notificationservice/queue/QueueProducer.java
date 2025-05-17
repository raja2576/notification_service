package com.PepSales_2576.notificationservice.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.PepSales_2576.notificationservice.modal.NotificationRequest;

@Component
public class QueueProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToQueue(NotificationRequest request) {
        rabbitTemplate.convertAndSend("notification.exchange", "notification.routingkey", request);
    }
}

