package com.company.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.company.Message;
import com.company.UserData;
import com.company.dto.UserBusinessDto;
import com.company.mapper.MessageToUserBusinessDtoMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserMessageListenerTest {

  @Mock
  private CityService cityService;

  @Mock
  private UserService userService;

  @Mock
  private MessageToUserBusinessDtoMapper mapper;

  @InjectMocks
  private UserMessageListener userMessageListener;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testConsumeMessage() {
    // Создаем данные для тестируемого Message и ConsumerRecord
    UserData userData = new UserData();
    userData.setFullName("Test User");
    userData.setPhone("123456789");
    userData.setCity("TestCity");
    userData.setGrade("A");
    userData.setTimestamp(1629187200L);

    Message testMessage = new Message();
    testMessage.setData(userData);

    ConsumerRecord<String, Message> record = new ConsumerRecord<>("topic", 0, 0L, "key", testMessage);

    // Устанавливаем поведение мока
    when(cityService.getCityIdByName("TestCity")).thenReturn(1L);

    UserBusinessDto userBusinessDto = new UserBusinessDto();
    userBusinessDto.setFullName("Test User");
    userBusinessDto.setPhone("123456789");
    userBusinessDto.setGrade("A");
    userBusinessDto.setCityId(1L);
    userBusinessDto.setTimestamp(userData.getTimestamp());

    when(mapper.toUserBusinessDto(any(Message.class), eq(1L))).thenReturn(userBusinessDto);

    // Выполняем тест
    userMessageListener.listen(record);

    // Проверяем что все методы вызваны
    verify(cityService).getCityIdByName("TestCity");
    verify(userService).updateUser(userBusinessDto);
  }

  @Test
  void testConsumeMessageCityServiceException() {
    // Создаем данные для тестируемого Message и ConsumerRecord
    UserData userData = new UserData();
    userData.setFullName("Test User");
    userData.setPhone("123456789");
    userData.setCity("TestCity");
    userData.setGrade("A");
    userData.setTimestamp(1629187200L);

    Message testMessage = new Message();
    testMessage.setData(userData);

    ConsumerRecord<String, Message> record = new ConsumerRecord<>("topic", 0, 0L, "key", testMessage);

    // Устанавливаем поведение мока для тестирования исключения
    when(cityService.getCityIdByName("TestCity")).thenThrow(new RuntimeException("City not found"));

    // Выполняем тест
    userMessageListener.listen(record);

    // Проверяем что updateUser не был вызван
    verify(userService, never()).updateUser(any(UserBusinessDto.class));
  }
}