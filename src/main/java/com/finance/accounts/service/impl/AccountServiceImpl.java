package com.finance.accounts.service.impl;

import com.finance.accounts.exception.AccountNotFoundException;
import com.finance.accounts.exception.AccountTypeNotFoundException;
import com.finance.accounts.model.dto.AccountCreateRequest;
import com.finance.accounts.model.dto.AccountDetailsDto;
import com.finance.accounts.model.dto.AccountDto;
import com.finance.accounts.model.dto.AccountUpdateRequest;
import com.finance.accounts.model.dto.AccountsPageResponse;
import com.finance.accounts.model.entity.Account;
import com.finance.accounts.model.entity.Account.AccountStatus;
import com.finance.accounts.model.entity.AccountDetails;
import com.finance.accounts.model.entity.AccountType;
import com.finance.accounts.model.event.AccountEvent;
import com.finance.accounts.model.event.AccountEvent.EventType;
import com.finance.accounts.repository.AccountDetailsRepository;
import com.finance.accounts.repository.AccountRepository;
import com.finance.accounts.repository.AccountTypeRepository;
import com.finance.accounts.service.AccountService;
import com.finance.accounts.service.KafkaProducerService;
import com.finance.accounts.service.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AccountService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final AccountTypeRepository accountTypeRepository;
  private final AccountDetailsRepository accountDetailsRepository;
  private final KafkaProducerService kafkaProducerService;
  private final UserService userService;

  @Override
  @Transactional
  public AccountDto createAccount(AccountCreateRequest request) {
    log.info("Creating account: {}", request.getAccountName());
    
    // Check if user exists
    if (!userService.userExists(request.getUserId())) {
      throw new IllegalArgumentException("User does not exist: " + request.getUserId());
    }
    
    // Get account type
    AccountType accountType = accountTypeRepository.findById(request.getAccountTypeId())
        .orElseThrow(() -> new AccountTypeNotFoundException(
            "Account type not found with ID: " + request.getAccountTypeId()));
    
    // Create and save account
    Account account = Account.builder()
        .accountName(request.getAccountName())
        .institutionName(request.getInstitutionName())
        .accountType(accountType)
        .accountStatus(AccountStatus.OPENING)
        .currency(request.getCurrency())
        .description(request.getDescription())
        .build();
    
    account = accountRepository.save(account);
    
    // Create and save account details if applicable
    if (needsAccountDetails(accountType.getAccountType())) {
      AccountDetails accountDetails = createAccountDetails(account, request);
      accountDetailsRepository.save(accountDetails);
    }
    
    // Send Kafka event
    sendAccountEvent(account, request.getUserId(), EventType.ACCOUNT_CREATED);
    
    // Set account status to OPEN after successful creation
    account.setAccountStatus(AccountStatus.OPEN);
    account = accountRepository.save(account);
    
    return mapToDto(account);
  }

  @Override
  public AccountDto getAccount(UUID accountNumber) {
    log.info("Getting account with number: {}", accountNumber);
    
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found with number: " + accountNumber));
    
    return mapToDto(account);
  }

  @Override
  public AccountsPageResponse getAccountsByUser(String userId, int page, int size) {
    log.info("Getting accounts for user: {}", userId);
    
    // Check if user exists
    if (!userService.userExists(userId)) {
      throw new IllegalArgumentException("User does not exist: " + userId);
    }
    
    Page<Account> accountsPage = accountRepository.findAllByUserId(
        userId, PageRequest.of(page, size));
    
    return AccountsPageResponse.builder()
        .accounts(accountsPage.getContent().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList()))
        .page(page)
        .size(size)
        .totalElements(accountsPage.getTotalElements())
        .totalPages(accountsPage.getTotalPages())
        .build();
  }

  @Override
  @Transactional
  public AccountDto updateAccount(UUID accountNumber, AccountUpdateRequest request) {
    log.info("Updating account with number: {}", accountNumber);
    
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found with number: " + accountNumber));
    
    // Update account fields if provided
    if (request.getAccountName() != null) {
      account.setAccountName(request.getAccountName());
    }
    
    if (request.getInstitutionName() != null) {
      account.setInstitutionName(request.getInstitutionName());
    }
    
    if (request.getAccountTypeId() != null) {
      AccountType newAccountType = accountTypeRepository.findById(request.getAccountTypeId())
          .orElseThrow(() -> new AccountTypeNotFoundException(
              "Account type not found with ID: " + request.getAccountTypeId()));
      account.setAccountType(newAccountType);
    }
    
    if (request.getCurrency() != null) {
      account.setCurrency(request.getCurrency());
    }
    
    if (request.getDescription() != null) {
      account.setDescription(request.getDescription());
    }
    
    account = accountRepository.save(account);
    
    // Update account details if they exist
    if (account.getAccountDetails() != null) {
      updateAccountDetails(account.getAccountDetails(), request);
    }
    
    // Send Kafka event
    sendAccountEvent(account, null, EventType.ACCOUNT_UPDATED);
    
    return mapToDto(account);
  }

  @Override
  @Transactional
  public AccountDto closeAccount(UUID accountNumber) {
    log.info("Closing account with number: {}", accountNumber);
    
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found with number: " + accountNumber));
    
    account.setAccountStatus(AccountStatus.CLOSING);
    account = accountRepository.save(account);
    
    // Send Kafka event
    sendAccountEvent(account, null, EventType.ACCOUNT_CLOSED);
    
    // Set account status to CLOSED after successful closure
    account.setAccountStatus(AccountStatus.CLOSED);
    account = accountRepository.save(account);
    
    return mapToDto(account);
  }
  
  /**
   * Maps an Account entity to an AccountDto.
   *
   * @param account the account entity
   * @return the account dto
   */
  private AccountDto mapToDto(Account account) {
    AccountDto accountDto = AccountDto.builder()
        .accountNumber(account.getAccountNumber())
        .accountName(account.getAccountName())
        .institutionName(account.getInstitutionName())
        .accountTypeId(account.getAccountType().getAccountTypeId())
        .accountTypeName(account.getAccountType().getAccountType())
        .accountStatus(account.getAccountStatus())
        .currency(account.getCurrency())
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt())
        .description(account.getDescription())
        .build();
    
    // Include account details if they exist
    if (account.getAccountDetails() != null) {
      accountDto.setAccountDetails(mapToDetailsDto(account.getAccountDetails()));
    }
    
    return accountDto;
  }
  
  /**
   * Maps AccountDetails entity to AccountDetailsDto.
   *
   * @param details the account details entity
   * @return the account details dto
   */
  private AccountDetailsDto mapToDetailsDto(AccountDetails details) {
    return AccountDetailsDto.builder()
        .interestRate(details.getInterestRate())
        .creditLimit(details.getCreditLimit())
        .loanAmount(details.getLoanAmount())
        .maturityDate(details.getMaturityDate())
        .investmentType(details.getInvestmentType())
        .build();
  }
  
  /**
   * Determines if an account type needs account details.
   *
   * @param accountType the type of account
   * @return true if the account type needs details, false otherwise
   */
  private boolean needsAccountDetails(String accountType) {
    return !accountType.equals("DIGITAL_WALLET");
  }
  
  /**
   * Creates an AccountDetails entity from an account and request.
   *
   * @param account the account entity
   * @param request the account creation request
   * @return the account details entity
   */
  private AccountDetails createAccountDetails(Account account, AccountCreateRequest request) {
    String accountType = account.getAccountType().getAccountType();
    
    AccountDetails details = new AccountDetails();
    details.setAccount(account);
    
    // Set fields based on account type
    if (accountType.equals("SAVINGS") || accountType.equals("CURRENT")) {
      details.setInterestRate(request.getInterestRate());
    } else if (accountType.equals("CREDIT_CARD")) {
      details.setCreditLimit(request.getCreditLimit());
    } else if (accountType.equals("LOAN")) {
      details.setLoanAmount(request.getLoanAmount());
      details.setInterestRate(request.getInterestRate());
      details.setMaturityDate(request.getMaturityDate());
    } else if (accountType.equals("INVESTMENT")) {
      details.setInvestmentType(request.getInvestmentType());
      details.setInterestRate(request.getInterestRate());
      details.setMaturityDate(request.getMaturityDate());
    }
    
    return details;
  }
  
  /**
   * Updates an existing AccountDetails entity with new values from a request.
   *
   * @param details the account details entity to update
   * @param request the update request with new values
   */
  private void updateAccountDetails(AccountDetails details, AccountUpdateRequest request) {
    if (request.getInterestRate() != null) {
      details.setInterestRate(request.getInterestRate());
    }
    
    if (request.getCreditLimit() != null) {
      details.setCreditLimit(request.getCreditLimit());
    }
    
    if (request.getLoanAmount() != null) {
      details.setLoanAmount(request.getLoanAmount());
    }
    
    if (request.getMaturityDate() != null) {
      details.setMaturityDate(request.getMaturityDate());
    }
    
    if (request.getInvestmentType() != null) {
      details.setInvestmentType(request.getInvestmentType());
    }
    
    accountDetailsRepository.save(details);
  }
  
  /**
   * Sends an account event to Kafka.
   *
   * @param account the account entity
   * @param userId the user ID (can be null for updates and closures)
   * @param eventType the type of event
   */
  private void sendAccountEvent(Account account, String userId, EventType eventType) {
    AccountEvent event = AccountEvent.builder()
        .accountNumber(account.getAccountNumber())
        .accountName(account.getAccountName())
        .institutionName(account.getInstitutionName())
        .accountType(account.getAccountType().getAccountType())
        .accountStatus(account.getAccountStatus())
        .currency(account.getCurrency())
        .userId(userId)
        .eventType(eventType)
        .timestamp(LocalDateTime.now())
        .build();
    
    kafkaProducerService.sendAccountEvent(event);
  }
}
