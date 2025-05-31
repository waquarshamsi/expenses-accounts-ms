package com.finance.accounts.service.impl;

import com.finance.accounts.exception.AccountTypeNotFoundException;
import com.finance.accounts.model.dto.AccountTypeDto;
import com.finance.accounts.model.entity.AccountType;
import com.finance.accounts.repository.AccountTypeRepository;
import com.finance.accounts.service.AccountTypeService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AccountTypeService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountTypeServiceImpl implements AccountTypeService {

  private final AccountTypeRepository accountTypeRepository;

  @Override
  @Cacheable(value = "accountTypes")
  public List<AccountTypeDto> getAllAccountTypes() {
    log.info("Getting all account types");
    return accountTypeRepository.findAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(value = "accountTypes", key = "#accountTypeId")
  public AccountTypeDto getAccountType(Integer accountTypeId) {
    log.info("Getting account type with ID: {}", accountTypeId);
    return accountTypeRepository.findById(accountTypeId)
        .map(this::mapToDto)
        .orElseThrow(() -> new AccountTypeNotFoundException(
            "Account type not found with ID: " + accountTypeId));
  }

  @Override
  @CachePut(value = "accountTypes", key = "#result.accountTypeId")
  public AccountTypeDto createAccountType(AccountTypeDto accountTypeDto) {
    log.info("Creating account type: {}", accountTypeDto.getAccountType());
    
    AccountType accountType = AccountType.builder()
        .accountType(accountTypeDto.getAccountType())
        .description(accountTypeDto.getDescription())
        .build();
    
    AccountType savedAccountType = accountTypeRepository.save(accountType);
    
    return mapToDto(savedAccountType);
  }

  @Override
  @CachePut(value = "accountTypes", key = "#accountTypeId")
  public AccountTypeDto updateAccountType(Integer accountTypeId, AccountTypeDto accountTypeDto) {
    log.info("Updating account type with ID: {}", accountTypeId);
    
    AccountType accountType = accountTypeRepository.findById(accountTypeId)
        .orElseThrow(() -> new AccountTypeNotFoundException(
            "Account type not found with ID: " + accountTypeId));
    
    accountType.setAccountType(accountTypeDto.getAccountType());
    accountType.setDescription(accountTypeDto.getDescription());
    
    AccountType updatedAccountType = accountTypeRepository.save(accountType);
    
    return mapToDto(updatedAccountType);
  }

  @Override
  @CacheEvict(value = "accountTypes", key = "#accountTypeId")
  public void deleteAccountType(Integer accountTypeId) {
    log.info("Deleting account type with ID: {}", accountTypeId);
    
    if (!accountTypeRepository.existsById(accountTypeId)) {
      throw new AccountTypeNotFoundException(
          "Account type not found with ID: " + accountTypeId);
    }
    
    accountTypeRepository.deleteById(accountTypeId);
  }
  
  /**
   * Maps an AccountType entity to an AccountTypeDto.
   *
   * @param accountType the account type entity
   * @return the account type dto
   */
  private AccountTypeDto mapToDto(AccountType accountType) {
    return AccountTypeDto.builder()
        .accountTypeId(accountType.getAccountTypeId())
        .accountType(accountType.getAccountType())
        .description(accountType.getDescription())
        .build();
  }
}
