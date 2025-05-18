package com.PepSales_2576.notificationservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class KeepAliveService {

 private static final String HEALTH_URL = "https://notification-service-3.onrender.com/health";

 @Scheduled(fixedRate = 300000) // every 5 minutes (in milliseconds)
 public void pingHealthCheck() {
     try {
         URL url = new URL(HEALTH_URL);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
         int responseCode = connection.getResponseCode();

         System.out.println("KeepAlive ping: " + HEALTH_URL + " - Response Code: " + responseCode);

         connection.disconnect();
     } catch (Exception e) {
         System.err.println("Error pinging keep-alive URL: " + e.getMessage());
     }
 }
}

