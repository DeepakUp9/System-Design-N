package com.espn.cricinfo.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application class for ESPN Cricinfo System.
 * Production-grade cricket scoring and analytics platform.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class CricinfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CricinfoApplication.class, args);
    }
}
