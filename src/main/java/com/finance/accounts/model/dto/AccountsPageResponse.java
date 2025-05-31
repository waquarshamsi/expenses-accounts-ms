package com.finance.accounts.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a paginated response of accounts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountsPageResponse {

  private List<AccountDto> accounts;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
}
