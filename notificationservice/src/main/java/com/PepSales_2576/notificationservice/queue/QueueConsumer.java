package com.PepSales_2576.notificationservice.queue;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

import com.PepSales_2576.notificationservice.modal.NotificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class QueueConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    private final int MAX_RETRIES = 3;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String sendGridFromEmail;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    @RabbitListener(queues = "notification.queue")
    public void receive(org.springframework.amqp.core.Message message, Channel channel) throws IOException {
        MessageProperties props = message.getMessageProperties();
        Map<String, Object> headers = props.getHeaders();

        NotificationRequest request = (NotificationRequest) messageConverter.fromMessage(message);

        try {
            if ("EMAIL".equalsIgnoreCase(request.getType())) {
                sendEmail(request);
            } else if ("SMS".equalsIgnoreCase(request.getType())) {
                sendSms(request);
            } else {
                System.out.println("In-App notification: " + request.getMessage());
            }
        } catch (Exception e) {
          int retries = 0;
            if (headers.get("x-retries") instanceof Integer) {
                retries = (int) headers.get("x-retries");
            }
            if (retries >= MAX_RETRIES) {
                System.err.println("Max retries exceeded. Sending to DLQ.");
                rabbitTemplate.convertAndSend("notification.dlq.exchange", "notification.dlq.routingkey", request);
            } else {
                System.out.println("Retrying notification. Attempt: " + (retries + 1));
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ignored) {}
                final int currentRetries = retries;
                rabbitTemplate.convertAndSend("notification.exchange", "notification.routingkey", request, m -> {
                    m.getMessageProperties().getHeaders().put("x-retries", currentRetries + 1);
                    return m;
          

                });
            }
        }
    }

    private void sendEmail(NotificationRequest request) throws IOException {
        Email from = new Email(sendGridFromEmail);
        String subject = request.getSubject();
        Email to = new Email("22052576@kiit.ac.in"); // TODO: replace with actual recipient email
        Content content = new Content("text/plain", request.getMessage());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request sgRequest = new Request();
        sgRequest.setMethod(Method.POST);
        sgRequest.setEndpoint("mail/send");
        sgRequest.setBody(mail.build());
        Response response = sg.api(sgRequest);

        System.out.println("SendGrid response status: " + response.getStatusCode());
    }

    private void sendSms(NotificationRequest request) {
        Message.creator(
                new PhoneNumber("+16603337508"), // TODO: replace with actual recipient phone number
                new PhoneNumber("+16603337508"), // TODO: replace with your Twilio number
                request.getMessage()
        ).create();
    }
}
