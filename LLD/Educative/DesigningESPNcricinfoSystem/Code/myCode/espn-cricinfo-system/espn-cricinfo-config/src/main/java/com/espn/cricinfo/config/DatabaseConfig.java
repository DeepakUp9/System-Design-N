package com.espn.cricinfo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database configuration for the ESPN Cricinfo system.
 * Configures JPA repositories, entity scanning, and transaction management.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.espn.cricinfo.infrastructure.repository")
@EntityScan(basePackages = "com.espn.cricinfo.infrastructure.entity")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    // JPA and database configurations are handled through application.yml
    // Additional custom configurations can be added here if needed
}
