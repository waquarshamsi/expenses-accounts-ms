package com.finance.accounts.model.event;

import com.finance.accounts.model.entity.Account.AccountStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event sent when an account is created, updated, or closed.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvent {

  private UUID accountNumber;
  private String accountName;
  private String institutionName;
  private String accountType;
  private AccountStatus accountStatus;
  private String currency;
  private String userId;
  private EventType eventType;
  private LocalDateTime timestamp;

  /**
   * Types of account events.
   */
  public enum EventType {
    ACCOUNT_CREATED,
    ACCOUNT_UPDATED,
    ACCOUNT_CLOSED
  }
}
