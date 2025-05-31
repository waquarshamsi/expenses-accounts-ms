package com.finance.accounts.service;

import com.finance.accounts.model.dto.AccountTypeDto;
import java.util.List;

/**
 * Service interface for account type operations.
 */
public interface AccountTypeService {

  /**
   * Get all account types.
   *
   * @return a list of all account types
   */
  List<AccountTypeDto> getAllAccountTypes();

  /**
   * Get an account type by its ID.
   *
   * @param accountTypeId the account type ID
   * @return the account type
   */
  AccountTypeDto getAccountType(Integer accountTypeId);

  /**
   * Create a new account type.
   *
   * @param accountTypeDto the account type to create
   * @return the created account type
   */
  AccountTypeDto createAccountType(AccountTypeDto accountTypeDto);

  /**
   * Update an account type.
   *
   * @param accountTypeId the account type ID
   * @param accountTypeDto the updated account type
   * @return the updated account type
   */
  AccountTypeDto updateAccountType(Integer accountTypeId, AccountTypeDto accountTypeDto);

  /**
   * Delete an account type.
   *
   * @param accountTypeId the account type ID
   */
  void deleteAccountType(Integer accountTypeId);
}
