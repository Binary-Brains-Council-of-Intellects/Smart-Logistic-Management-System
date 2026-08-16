package com.binarybrains.slms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Smart Logistics Management System (SLMS)
 * Main entry point for the Spring Boot REST API application.
 */
@SpringBootApplication
@EnableMongoRepositories(considerNestedRepositories = true)
public class SlmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlmsApplication.class, args);
    }

    /**
     * Global CORS Configuration allowing local ReactJS frontend (Vite) communication.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
