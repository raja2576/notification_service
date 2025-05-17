package com.PepSales_2576.notificationservice.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import com.PepSales_2576.notificationservice.config.EnvConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailSender {

    private final String sendGridApiKey = EnvConfig.getEnv("SENDGRID_API_KEY");

    // fallback default sender email
    private final String fromEmail = "22052576@kiit.ac.in";

    public EmailSender() {
        if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
            throw new IllegalStateException("SENDGRID_API_KEY is missing from environment variables.");
        }
    }

    public void sendEmail(String to, String subject, String contentText) {
        if (to == null || to.isEmpty() || subject == null || contentText == null) {
            throw new IllegalArgumentException("To address, subject, and content must be non-null and valid.");
        }

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("SendGrid response status: " + response.getStatusCode());
            System.out.println("SendGrid response body: " + response.getBody());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
