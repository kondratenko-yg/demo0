package com.company.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Сущность для представления проблемных сообщений из Kafka.
 */
@Table("error_message_data")
@Data
public class ErrorMessageData {

  @Id
  private Long id;
  private String message;
  private String errorMessage;
  private Long timestamp;
  private Integer attemptCount;

  public ErrorMessageData() {
    this.attemptCount = 0; // Инициализация количества попыток
  }
}
