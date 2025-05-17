package com.PepSales_2576.notificationservice.config;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SmsSender {

    private final String accountSid = EnvConfig.getEnv("TWILIO_ACCOUNT_SID");
    private final String authToken = EnvConfig.getEnv("TWILIO_AUTH_TOKEN");
    private final String fromPhoneNumber = EnvConfig.getEnv("TWILIO_PHONE_NUMBER");

    @PostConstruct
    public void init() {
        if (accountSid == null || authToken == null || fromPhoneNumber == null) {
            throw new IllegalStateException("Twilio credentials or phone number are not properly set!");
        }
        System.out.println("Initializing Twilio with SID: " + accountSid.substring(0, 5) + "...");
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String toPhoneNumber, String message) {
        if (toPhoneNumber == null || message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Phone number and message must not be null or empty");
        }

        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(fromPhoneNumber),
                message
        ).create();

        System.out.println("SMS sent to " + toPhoneNumber);
    }
}
