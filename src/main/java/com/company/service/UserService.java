package com.company.service;

import com.company.client.UserClient;
import com.company.dto.UserBusinessDto;
import com.company.dto.UpdateUserRequest;
import com.company.exception.UserServiceException;
import com.company.mapper.MessageToUserBusinessDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserClient usersClient;
  private final MessageToUserBusinessDtoMapper mapper;

  /**
   * Обрабатывает обновление информации о пользователе, используя UserClient для отправки данных.
   *
   * @param userBusinessDto DTO с бизнес-информацией о пользователе.
   * @throws UserServiceException если произошла ошибка при взаимодействии с Users API
   */
  public void updateUser(UserBusinessDto userBusinessDto) {
    try {
      // Преобразование объекта UserBusinessDto в UpdateUserRequest
      UpdateUserRequest updateUserRequest = mapper.toUpdateUserRequest(userBusinessDto);

      // Отправка запроса на обновление пользователя через UserClient
      usersClient.updateUser(updateUserRequest);
    } catch (HttpClientErrorException e) {
      throw new UserServiceException("Ошибка при вызове API Users: " + e.getMessage(), e);
    }
  }
}
