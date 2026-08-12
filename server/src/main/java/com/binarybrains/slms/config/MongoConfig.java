package com.binarybrains.slms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration.
 *
 * EnableMongoRepositories scans for repository interfaces.
 * EnableMongoAuditing enables @CreatedDate / @LastModifiedDate support.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.binarybrains.slms")
@EnableMongoAuditing
public class MongoConfig {
    // Spring Boot auto-configures the MongoClient from application.yml.
    // This class enables auditing and explicit repository scanning.
}
