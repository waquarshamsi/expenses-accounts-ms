package com.finance.accounts.controller;

import com.finance.accounts.model.dto.AccountCreateRequest;
import com.finance.accounts.model.dto.AccountDto;
import com.finance.accounts.model.dto.AccountUpdateRequest;
import com.finance.accounts.model.dto.AccountsPageResponse;
import com.finance.accounts.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for account operations.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account", description = "Account management APIs")
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  @Operation(
      summary = "Create a new account",
      description = "Creates a new financial account",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<AccountDto> createAccount(
      @Valid @RequestBody AccountCreateRequest request) {
    log.info("Received request to create account: {}", request.getAccountName());
    return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
  }

  @GetMapping("/{accountNumber}")
  @PreAuthorize("hasRole('USER')")
  @Operation(
      summary = "Get account by number",
      description = "Returns account details for the given account number",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<AccountDto> getAccount(
      @Parameter(description = "Account number (UUID)", required = true)
      @PathVariable UUID accountNumber) {
    log.info("Received request to get account: {}", accountNumber);
    return ResponseEntity.ok(accountService.getAccount(accountNumber));
  }

  @GetMapping("/user/{userId}")
  @PreAuthorize("hasRole('USER')")
  @Operation(
      summary = "Get accounts by user",
      description = "Returns paginated list of accounts for a user",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<AccountsPageResponse> getAccountsByUser(
      @Parameter(description = "User ID", required = true)
      @PathVariable String userId,
      @Parameter(description = "Page number (0-based)", in = ParameterIn.QUERY)
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size", in = ParameterIn.QUERY)
      @RequestParam(defaultValue = "10") int size) {
    log.info("Received request to get accounts for user: {}", userId);
    return ResponseEntity.ok(accountService.getAccountsByUser(userId, page, size));
  }

  @PutMapping("/{accountNumber}")
  @PreAuthorize("hasRole('USER')")
  @Operation(
      summary = "Update an account",
      description = "Updates an existing account",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<AccountDto> updateAccount(
      @Parameter(description = "Account number (UUID)", required = true)
      @PathVariable UUID accountNumber,
      @Valid @RequestBody AccountUpdateRequest request) {
    log.info("Received request to update account: {}", accountNumber);
    return ResponseEntity.ok(accountService.updateAccount(accountNumber, request));
  }

  @DeleteMapping("/{accountNumber}")
  @PreAuthorize("hasRole('USER')")
  @Operation(
      summary = "Close an account",
      description = "Soft deletes an account by setting its status to CLOSED",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<AccountDto> closeAccount(
      @Parameter(description = "Account number (UUID)", required = true)
      @PathVariable UUID accountNumber) {
    log.info("Received request to close account: {}", accountNumber);
    return ResponseEntity.ok(accountService.closeAccount(accountNumber));
  }
}
