package com.PepSales_2576.notificationservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

 @GetMapping("/health")
 public String healthCheck() {
     return "OK";
 }
}
