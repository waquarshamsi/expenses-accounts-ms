package com.finance.accounts.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for the AccountDetails entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsDto {

  private BigDecimal interestRate;
  private BigDecimal creditLimit;
  private BigDecimal loanAmount;
  private LocalDateTime maturityDate;
  private String investmentType;
}
