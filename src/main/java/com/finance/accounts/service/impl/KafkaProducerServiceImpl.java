package com.finance.accounts.service.impl;

import com.finance.accounts.model.event.AccountEvent;
import com.finance.accounts.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the KafkaProducerService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

  private final KafkaTemplate<String, AccountEvent> kafkaTemplate;

  @Value("${kafka.topic.account-events:account.events}")
  private String accountEventsTopic;

  @Override
  @Retryable(
      value = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void sendAccountEvent(AccountEvent event) {
    log.info("Sending account event: {}", event);
    
    CompletableFuture<SendResult<String, AccountEvent>> future = kafkaTemplate.send(
        accountEventsTopic, event.getAccountNumber().toString(), event);
    
    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("Account event sent successfully: {}", 
            result.getRecordMetadata().offset());
      } else {
        log.error("Failed to send account event: {}", ex.getMessage());
      }
    });
  }
}
