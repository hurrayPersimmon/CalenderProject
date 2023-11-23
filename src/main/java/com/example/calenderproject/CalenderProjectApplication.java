package com.example.calenderproject;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class CalenderProjectApplication {


    public static void main(String[] args) {
        SpringApplication.run(CalenderProjectApplication.class, args);
    }

}


