package com.finance.accounts.service;

import com.finance.accounts.model.event.AccountEvent;

/**
 * Service interface for sending Kafka events.
 */
public interface KafkaProducerService {

  /**
   * Sends an account event to Kafka.
   *
   * @param event the account event to send
   */
  void sendAccountEvent(AccountEvent event);
}
