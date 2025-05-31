package com.finance.accounts.controller;

import com.finance.accounts.model.dto.AccountTypeDto;
import com.finance.accounts.service.AccountTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for account type operations.
 */
@RestController
@RequestMapping("/account-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Type", description = "Account type management APIs")
public class AccountTypeController {

  private final AccountTypeService accountTypeService;

  @GetMapping
  @Operation(
      summary = "Get all account types",
      description = "Returns a list of all available account types"
  )
  public ResponseEntity<List<AccountTypeDto>> getAllAccountTypes() {
    log.info("Received request to get all account types");
    return ResponseEntity.ok(accountTypeService.getAllAccountTypes());
  }

  @GetMapping("/{accountTypeId}")
  @Operation(
      summary = "Get account type by ID",
      description = "Returns account type details for the given ID"
  )
  public ResponseEntity<AccountTypeDto> getAccountType(
      @Parameter(description = "Account type ID", required = true)
      @PathVariable Integer accountTypeId) {
    log.info("Received request to get account type: {}", accountTypeId);
    return ResponseEntity.ok(accountTypeService.getAccountType(accountTypeId));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(
      summary = "Create a new account type",
      description = "Creates a new account type (admin only)",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<AccountTypeDto> createAccountType(
      @Valid @RequestBody AccountTypeDto accountTypeDto) {
    log.info("Received request to create account type: {}", accountTypeDto.getAccountType());
    return new ResponseEntity<>(
        accountTypeService.createAccountType(accountTypeDto), HttpStatus.CREATED);
  }

  @PutMapping("/{accountTypeId}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(
      summary = "Update an account type",
      description = "Updates an existing account type (admin only)",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<AccountTypeDto> updateAccountType(
      @Parameter(description = "Account type ID", required = true)
      @PathVariable Integer accountTypeId,
      @Valid @RequestBody AccountTypeDto accountTypeDto) {
    log.info("Received request to update account type: {}", accountTypeId);
    return ResponseEntity.ok(
        accountTypeService.updateAccountType(accountTypeId, accountTypeDto));
  }

  @DeleteMapping("/{accountTypeId}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(
      summary = "Delete an account type",
      description = "Deletes an account type (admin only)",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<Void> deleteAccountType(
      @Parameter(description = "Account type ID", required = true)
      @PathVariable Integer accountTypeId) {
    log.info("Received request to delete account type: {}", accountTypeId);
    accountTypeService.deleteAccountType(accountTypeId);
    return ResponseEntity.noContent().build();
  }
}
