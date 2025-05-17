package com.PepSales_2576.notificationservice.config;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    private static Dotenv dotenv = Dotenv.load();

    public static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = dotenv.get(key);
        }
        if (value == null) {
            System.err.println("WARNING: Environment variable '" + key + "' is not set.");
        }
        return value;
    }

}

