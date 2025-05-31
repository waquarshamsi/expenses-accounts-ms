package com.finance.accounts.exception;

/**
 * Exception thrown when an account type is not found.
 */
public class AccountTypeNotFoundException extends RuntimeException {

  public AccountTypeNotFoundException(String message) {
    super(message);
  }
}
