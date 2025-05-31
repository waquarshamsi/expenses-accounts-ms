package com.finance.accounts.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the User Service.
 */
@Configuration
@ConfigurationProperties(prefix = "service.user")
@Data
public class UserServiceProperties {

  /**
   * Base URL of the User Service.
   */
  private String baseUrl = "http://user-service:8080";
}
