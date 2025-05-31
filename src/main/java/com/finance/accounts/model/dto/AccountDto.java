package com.finance.accounts.model.dto;

import com.finance.accounts.model.entity.Account.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for the Account entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

  private UUID accountNumber;

  @NotBlank(message = "Account name is required")
  @Size(max = 20, message = "Account name must be at most 20 characters")
  private String accountName;

  @NotBlank(message = "Institution name is required")
  @Size(max = 50, message = "Institution name must be at most 50 characters")
  private String institutionName;

  @NotNull(message = "Account type ID is required")
  private Integer accountTypeId;

  private String accountTypeName;

  @NotNull(message = "Account status is required")
  private AccountStatus accountStatus;

  @NotBlank(message = "Currency is required")
  @Size(max = 10, message = "Currency must be at most 10 characters")
  private String currency;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String description;

  // AccountDetails-related fields that may be included based on account type
  private AccountDetailsDto accountDetails;
}
