package com.finance.accounts.service.impl;

import com.finance.accounts.config.UserServiceProperties;
import com.finance.accounts.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of the UserService interface.
 * Uses REST calls to the User Microservice to validate users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final RestTemplate restTemplate;
  private final UserServiceProperties userServiceProperties;

  @Override
  @Retryable(
      value = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public boolean userExists(String userId) {
    log.info("Checking if user exists: {}", userId);
    
    try {
      String url = userServiceProperties.getBaseUrl() + "/users/" + userId + "/exists";
      ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
      
      return response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody());
    } catch (Exception e) {
      log.error("Error checking if user exists: {}", e.getMessage());
      return false;
    }
  }
}
