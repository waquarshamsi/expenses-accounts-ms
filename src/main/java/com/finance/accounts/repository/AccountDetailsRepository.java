package com.finance.accounts.repository;

import com.finance.accounts.model.entity.AccountDetails;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for AccountDetails entity.
 */
@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, UUID> {
}
