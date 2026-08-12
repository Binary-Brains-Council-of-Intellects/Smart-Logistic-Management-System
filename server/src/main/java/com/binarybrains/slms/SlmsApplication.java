package com.binarybrains.slms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Smart Logistics Management System (SLMS)
 *
 * Main entry point for the Spring Boot application.
 * All Spring beans (Services, Repositories, Controllers) are managed
 * as singletons by default — demonstrating Singleton pattern via Spring IoC.
 */
@SpringBootApplication
public class SlmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlmsApplication.class, args);
    }
}
