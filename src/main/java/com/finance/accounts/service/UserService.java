package com.finance.accounts.service;

/**
 * Service interface for user-related operations.
 */
public interface UserService {

  /**
   * Checks if a user exists.
   *
   * @param userId the user ID
   * @return true if the user exists, false otherwise
   */
  boolean userExists(String userId);
}
