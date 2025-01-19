package com.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для передачи информации о пользователе.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBusinessDto{
  private String fullName;

  private String phone;

  private Long cityId;

  private String grade;

  private Long timestamp;
}

