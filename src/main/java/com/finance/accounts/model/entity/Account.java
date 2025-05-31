package com.finance.accounts.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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
 * Entity representing a financial account.
 */
@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id
  @Column(name = "account_number")
  private UUID accountNumber;

  @NotBlank
  @Size(max = 20)
  @Column(name = "account_name")
  private String accountName;

  @NotBlank
  @Size(max = 50)
  @Column(name = "institution_name")
  private String institutionName;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_type_id", nullable = false)
  private AccountType accountType;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "account_status")
  private AccountStatus accountStatus;

  @NotBlank
  @Size(max = 10)
  @Column(name = "currency")
  private String currency;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "description")
  private String description;

  @NotBlank
  @Column(name = "user_id")
  private String userId;

  @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
  private AccountDetails accountDetails;

  @PrePersist
  protected void onCreate() {
    if (accountNumber == null) {
      accountNumber = UUID.randomUUID();
    }
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  /**
   * Enum representing the different statuses an account can have.
   */
  public enum AccountStatus {
    OPEN, CLOSED, OPENING, CLOSING, LOCKED
  }
}
