package com.company.service;

import com.company.client.UserClient;
import com.company.dto.UpdateUserRequest;
import com.company.dto.UserBusinessDto;
import com.company.exception.UserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserClient usersClient;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUpdateUserSuccess() {
    // Создаем тестируемый UserBusinessDto
    UserBusinessDto userBusinessDto = new UserBusinessDto();
    userBusinessDto.setFullName("Test User");
    userBusinessDto.setPhone("123456789");
    userBusinessDto.setCityId(1L);
    userBusinessDto.setGrade("A");
    userBusinessDto.setTimestamp(1629187200L);

    // Создаем ожидаемый UpdateUserRequest
    UpdateUserRequest updateUserRequest = new UpdateUserRequest();
    updateUserRequest.setFio("Test User");
    updateUserRequest.setPhone("123456789");
    updateUserRequest.setCityId(1);
    updateUserRequest.setGrade("A");
    updateUserRequest.setTimestamp((int)(userBusinessDto.getTimestamp() / 1000));

    // Настраиваем Mock для успешного вызова
    doNothing().when(usersClient).updateUser(any(UpdateUserRequest.class));

    // Выполняем тест
    userService.updateUser(userBusinessDto);

    // Проверяем, что updateUser был вызван с правильным аргументом
    verify(usersClient).updateUser(eq(updateUserRequest));
  }

  @Test
  void testUpdateUserFailure() {
    // Создаем тестируемый UserBusinessDto
    UserBusinessDto userBusinessDto = new UserBusinessDto();
    userBusinessDto.setFullName("Test User");
    userBusinessDto.setPhone("123456789");
    userBusinessDto.setCityId(1L);
    userBusinessDto.setGrade("A");
    userBusinessDto.setTimestamp(1629187200L);

    // Настраиваем Mock для выброса исключения
    doThrow(new RuntimeException("Update failed")).when(usersClient).updateUser(any(UpdateUserRequest.class));

    // Выполняем тест и проверяем, что UserServiceException выброшено
    assertThrows(UserServiceException.class, () -> userService.updateUser(userBusinessDto));

    // Проверяем, что updateUser был вызван один раз
    verify(usersClient).updateUser(any(UpdateUserRequest.class));
  }
}
