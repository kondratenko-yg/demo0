package com.company.service;

import com.company.client.UsersClient;
import com.company.dto.UpdateUserRequest;
import com.company.service.dto.UserDto;
import com.company.mapper.MessageToUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с информацией о пользователях.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UsersClient usersClient;
  private final MessageToUserMapper messageToUserMapper;

  /**
   * Обрабатывает информацию о пользователе и отправляет запрос на обновление.
   *
   * @param userDto объект с бизнес-логикой пользователя
   */
  public void processAndUpdateUser(UserDto userDto) {

    // Преобразование UserDto в UpdateUserRequest
    UpdateUserRequest updateUserRequest = messageToUserMapper.toUpdateUserRequest(userDto);

    // Отправка запроса на обновление данных пользователя
    usersClient.updateUser(updateUserRequest);
  }
}
