package com.finance.accounts.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.accounts.model.entity.Account;
import com.finance.accounts.model.entity.Account.AccountStatus;
import com.finance.accounts.model.entity.AccountType;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AccountRepositoryTest {

  @Container
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14-alpine")
      .withDatabaseName("accounts_test_db")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountTypeRepository accountTypeRepository;

  private AccountType savedAccountType;
  private Account savedAccount;
  private String userId = "test-user-id";

  @BeforeEach
  void setUp() {
    // Create an account type
    AccountType accountType = AccountType.builder()
        .accountType("SAVINGS")
        .description("Savings Account")
        .build();
    savedAccountType = accountTypeRepository.save(accountType);

    // Create an account
    Account account = Account.builder()
        .accountNumber(UUID.randomUUID())
        .accountName("Test Account")
        .institutionName("Test Bank")
        .accountType(savedAccountType)
        .accountStatus(AccountStatus.OPEN)
        .currency("USD")
        .description("Test Description")
        .build();
    savedAccount = accountRepository.save(account);
  }

  @Test
  void findByAccountNumber_ShouldReturnAccount() {
    // When
    Optional<Account> foundAccount = accountRepository.findByAccountNumber(savedAccount.getAccountNumber());

    // Then
    assertThat(foundAccount).isPresent();
    assertThat(foundAccount.get().getAccountName()).isEqualTo("Test Account");
    assertThat(foundAccount.get().getAccountType().getAccountType()).isEqualTo("SAVINGS");
  }

  @Test
  void findByInstitutionName_ShouldReturnAccounts() {
    // When
    var accounts = accountRepository.findByInstitutionName("Test Bank");

    // Then
    assertThat(accounts).hasSize(1);
    assertThat(accounts.get(0).getAccountName()).isEqualTo("Test Account");
  }

  @Test
  void findByAccountStatus_ShouldReturnAccounts() {
    // When
    var accounts = accountRepository.findByAccountStatus(AccountStatus.OPEN);

    // Then
    assertThat(accounts).hasSize(1);
    assertThat(accounts.get(0).getAccountStatus()).isEqualTo(AccountStatus.OPEN);
  }
}
