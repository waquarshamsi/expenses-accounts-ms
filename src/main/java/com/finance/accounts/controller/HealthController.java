package com.finance.accounts.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for health check endpoints.
 */
@RestController
@RequestMapping("/health")
public class HealthController {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${spring.profiles.active:default}")
  private String activeProfile;

  /**
   * Simple health check endpoint.
   *
   * @return health status
   */
  @GetMapping
  public ResponseEntity<Map<String, Object>> healthCheck() {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "UP");
    response.put("service", applicationName);
    response.put("profile", activeProfile);
    
    return ResponseEntity.ok(response);
  }
}
