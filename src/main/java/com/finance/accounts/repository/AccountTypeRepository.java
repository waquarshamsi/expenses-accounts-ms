package com.finance.accounts.repository;

import com.finance.accounts.model.entity.AccountType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for AccountType entity.
 */
@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {

  /**
   * Find an account type by its name.
   *
   * @param accountType the name of the account type
   * @return the account type if found
   */
  Optional<AccountType> findByAccountType(String accountType);
}
