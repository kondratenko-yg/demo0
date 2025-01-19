package com.company.service.impl;

import com.company.client.UserClient;
import com.company.dto.UserDto;
import com.company.dto.UpdateUserRequest;
import com.company.exception.UserServiceException;
import com.company.mapper.MessageToUserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

  private final UserClient usersClient;
  private final MessageToUserDtoMapper mapper;

  /**
   * Обрабатывает обновление информации о пользователе, используя UserClient для отправки данных.
   *
   * @param userDto DTO с бизнес-информацией о пользователе.
   * @throws UserServiceException если произошла ошибка при взаимодействии с Users API
   */
  public void updateUser(UserDto userDto) {
    try {
      // Преобразование объекта UserDto в UpdateUserRequest
      UpdateUserRequest updateUserRequest = mapper.toUpdateUserRequest(userDto);

      // Отправка запроса на обновление пользователя через UserClient
      usersClient.updateUser(updateUserRequest);
    } catch (HttpClientErrorException e) {
      throw new UserServiceException("Ошибка при вызове API Users: " + e.getMessage(), e);
    }
  }
}
