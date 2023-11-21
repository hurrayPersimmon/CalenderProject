package com.example.calenderproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class CalenderProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalenderProjectApplication.class, args);
    }

}
