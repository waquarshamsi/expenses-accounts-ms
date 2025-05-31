package com.finance.accounts.repository;

import com.finance.accounts.model.entity.Account;
import com.finance.accounts.model.entity.Account.AccountStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Account entity.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

  /**
   * Find all accounts for a specific user.
   *
   * @param userId the ID of the user
   * @param pageable pagination information
   * @return a page of accounts
   */
  Page<Account> findAllByUserId(String userId, Pageable pageable);

  /**
   * Find an account by its number.
   *
   * @param accountNumber the account number
   * @return the account if found
   */
  Optional<Account> findByAccountNumber(UUID accountNumber);

  /**
   * Find all accounts by status.
   *
   * @param status the account status
   * @return a list of accounts with the given status
   */
  List<Account> findByAccountStatus(AccountStatus status);

  /**
   * Find all accounts by institution name.
   *
   * @param institutionName the name of the institution
   * @return a list of accounts from the given institution
   */
  List<Account> findByInstitutionName(String institutionName);
}
