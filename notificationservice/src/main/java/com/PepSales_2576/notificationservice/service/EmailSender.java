package com.PepSales_2576.notificationservice.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailSender {

    
    private String sendGridApiKey;

     // fallback to default if not set
    private String fromEmail="22052576@kiit.ac.in";
    
    public EmailSender() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")  // Adjust if your .env is not in root
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        this.sendGridApiKey = dotenv.get("SENDGRID_API_KEY");

        if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
            throw new IllegalStateException("SENDGRID_API_KEY is missing from .env file.");
        }
    }

    public void sendEmail(String to, String subject, String contentText) {
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
