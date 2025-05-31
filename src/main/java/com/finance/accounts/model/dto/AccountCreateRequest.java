package com.finance.accounts.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for account creation requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {

  private String accountName;
  private String institutionName;
  private Integer accountTypeId;
  private String currency;
  private String description;
  private String userId;
  
  // Details based on account type
  private BigDecimal interestRate;
  private BigDecimal creditLimit;
  private BigDecimal loanAmount;
  private LocalDateTime maturityDate;
  private String investmentType;
}
