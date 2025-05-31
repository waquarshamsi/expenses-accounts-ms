package com.finance.accounts.service;

import com.finance.accounts.model.dto.AccountCreateRequest;
import com.finance.accounts.model.dto.AccountDto;
import com.finance.accounts.model.dto.AccountUpdateRequest;
import com.finance.accounts.model.dto.AccountsPageResponse;
import java.util.UUID;

/**
 * Service interface for account operations.
 */
public interface AccountService {

  /**
   * Create a new account.
   *
   * @param request the account creation request
   * @return the created account
   */
  AccountDto createAccount(AccountCreateRequest request);

  /**
   * Get an account by its number.
   *
   * @param accountNumber the account number
   * @return the account
   */
  AccountDto getAccount(UUID accountNumber);

  /**
   * Get all accounts for a user with pagination.
   *
   * @param userId the user ID
   * @param page the page number
   * @param size the page size
   * @return a paginated response of accounts
   */
  AccountsPageResponse getAccountsByUser(String userId, int page, int size);

  /**
   * Update an account.
   *
   * @param accountNumber the account number
   * @param request the update request
   * @return the updated account
   */
  AccountDto updateAccount(UUID accountNumber, AccountUpdateRequest request);

  /**
   * Close (soft delete) an account.
   *
   * @param accountNumber the account number
   * @return the closed account
   */
  AccountDto closeAccount(UUID accountNumber);
}
