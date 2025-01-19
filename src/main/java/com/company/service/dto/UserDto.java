package com.company.service.dto;

import lombok.Data;

/**
 * Класс DTO, представляющий информацию о пользователе.
 */
@Data
public class UserDto {

  /**
   * Полное имя пользователя.
   */
  private String fullName;

  /**
   * Телефон пользователя.
   */
  private String phone;

  /**
   * Идентификатор города пользователя.
   */
  private Long city;

  /**
   * Грейд пользователя.
   */
  private String grade;

  /**
   * Время обновления информации.
   */
  private long timestamp;
}
