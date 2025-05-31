package com.finance.accounts.config;

import com.finance.accounts.model.event.AccountEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Configuration for Kafka.
 */
@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  /**
   * Configures the Kafka producer factory.
   *
   * @return the producer factory
   */
  @Bean
  public ProducerFactory<String, AccountEvent> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
        org.apache.kafka.common.serialization.StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configProps.put(ProducerConfig.ACKS_CONFIG, "all");
    configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
    
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  /**
   * Creates a Kafka template.
   *
   * @return the Kafka template
   */
  @Bean
  public KafkaTemplate<String, AccountEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
