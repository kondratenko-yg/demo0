package com.company.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая проблемное сообщение.
 */
@Data
@Table("problem_messages")
public class ProblemMessage {

  /**
   * Идентификатор записи.
   */
  @Id
  private Long id;

  /**
   * Содержание проблемного сообщения.
   */
  private String messageContent;

  /**
   * Время попытки обработки или сохранения.
   */
  private LocalDateTime timestamp;

  /**
   * Количество попыток обработки.
   */
  private int attempts;

  // Конструкторы, геттеры и сеттеры будут сгенерированы Lombok
}
