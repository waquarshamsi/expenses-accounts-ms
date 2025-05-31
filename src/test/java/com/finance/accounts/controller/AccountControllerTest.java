package com.finance.accounts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.accounts.model.dto.AccountCreateRequest;
import com.finance.accounts.model.dto.AccountDto;
import com.finance.accounts.model.dto.AccountsPageResponse;
import com.finance.accounts.model.entity.Account.AccountStatus;
import com.finance.accounts.service.AccountService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AccountService accountService;

  private AccountDto mockAccountDto;
  private AccountCreateRequest mockCreateRequest;
  private UUID mockAccountNumber;

  @BeforeEach
  void setUp() {
    mockAccountNumber = UUID.randomUUID();
    
    mockAccountDto = AccountDto.builder()
        .accountNumber(mockAccountNumber)
        .accountName("Test Account")
        .institutionName("Test Bank")
        .accountTypeId(1)
        .accountTypeName("SAVINGS")
        .accountStatus(AccountStatus.OPEN)
        .currency("USD")
        .build();
    
    mockCreateRequest = AccountCreateRequest.builder()
        .accountName("Test Account")
        .institutionName("Test Bank")
        .accountTypeId(1)
        .currency("USD")
        .userId("user123")
        .build();
  }

  @Test
  @WithMockUser(roles = "USER")
  void createAccount_ShouldReturnCreated() throws Exception {
    when(accountService.createAccount(any(AccountCreateRequest.class))).thenReturn(mockAccountDto);

    mockMvc.perform(post("/accounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockCreateRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.accountNumber").value(mockAccountNumber.toString()))
        .andExpect(jsonPath("$.accountName").value("Test Account"))
        .andExpect(jsonPath("$.accountStatus").value("OPEN"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void getAccount_ShouldReturnAccount() throws Exception {
    when(accountService.getAccount(eq(mockAccountNumber))).thenReturn(mockAccountDto);

    mockMvc.perform(get("/accounts/{accountNumber}", mockAccountNumber))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accountNumber").value(mockAccountNumber.toString()))
        .andExpect(jsonPath("$.accountName").value("Test Account"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void getAccountsByUser_ShouldReturnAccounts() throws Exception {
    AccountsPageResponse pageResponse = AccountsPageResponse.builder()
        .accounts(List.of(mockAccountDto))
        .page(0)
        .size(10)
        .totalElements(1)
        .totalPages(1)
        .build();
    
    when(accountService.getAccountsByUser(eq("user123"), anyInt(), anyInt()))
        .thenReturn(pageResponse);

    mockMvc.perform(get("/accounts/user/{userId}", "user123")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accounts[0].accountNumber").value(mockAccountNumber.toString()))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void getAccount_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
    mockMvc.perform(get("/accounts/{accountNumber}", mockAccountNumber))
        .andExpect(status().isUnauthorized());
  }
}
