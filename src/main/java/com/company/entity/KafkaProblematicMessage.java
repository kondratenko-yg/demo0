package com.company.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Сущность для представления проблемных сообщений из Kafka.
 */
@Table("kafka_problematic_messages")
@Data
public class KafkaProblematicMessage {

  @Id
  private Long id;
  private String key;
  private String message;
  private String errorMessage;
  private LocalDateTime timestamp;
  private Integer attemptCount;

  public KafkaProblematicMessage() {
    this.attemptCount = 0; // Инициализация количества попыток
  }
}
