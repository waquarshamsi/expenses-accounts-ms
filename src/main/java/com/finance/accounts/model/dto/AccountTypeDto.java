package com.finance.accounts.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for the AccountType entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeDto {

  private Integer accountTypeId;

  @NotBlank(message = "Account type is required")
  @Size(max = 50, message = "Account type must be at most 50 characters")
  private String accountType;

  @Size(max = 100, message = "Description must be at most 100 characters")
  private String description;
}
