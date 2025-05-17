package com.PepSales_2576.notificationservice.config;

import com.twilio.Twilio;  
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SmsSender {

    private final String accountSid = EnvConfig.getEnv("TWILIO_ACCOUNT_SID");
    private final String authToken = EnvConfig.getEnv("TWILIO_AUTH_TOKEN");
    private final String fromPhoneNumber = EnvConfig.getEnv("TWILIO_PHONE_NUMBER");

    @PostConstruct
    public void init() {
        if (accountSid == null || authToken == null) {
            throw new IllegalStateException("Twilio credentials are not set!");
        }
        System.out.println("Twilio SID: " + accountSid.substring(0, 5) + "...");
        Twilio.init(accountSid, authToken);
    }



    public void sendSms(String toPhoneNumber, String message) {
        Message.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber),
                new com.twilio.type.PhoneNumber(fromPhoneNumber),
                message
        ).create();

        System.out.println("SMS sent to " + toPhoneNumber);
    }
}
