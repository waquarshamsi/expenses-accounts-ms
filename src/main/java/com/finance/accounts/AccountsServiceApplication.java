package com.finance.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Main application class for the Accounts Microservice.
 * This service manages various types of financial accounts.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableKafka
@EnableRetry
public class AccountsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountsServiceApplication.class, args);
  }
}
