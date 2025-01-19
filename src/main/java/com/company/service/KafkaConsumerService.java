package com.company.service;

import com.company.Message;
import com.company.entity.ProblemMessage;
import com.company.exception.CityNotFoundException;
import com.company.repository.ProblemMessageRepository;
import com.company.service.CityService;
import com.company.service.UserService;
import com.company.service.dto.UserDto;
import com.company.mapper.MessageToUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

  private final CityService cityService;
  private final UserService userService;
  private final MessageToUserMapper messageToUserMapper;
  private final ProblemMessageRepository problemMessageRepository;
  private final ObjectMapper objectMapper; // Используем Jackson для работы с JSON

  /**
   * Метод для обработки сообщений из Kafka.
   *
   * @param record сообщение из Kafka
   */
  @KafkaListener(topics = "user-topic", groupId = "user-group")
  public void consume(ConsumerRecord<String, Message> record) {
    Message message = record.value();

    try {
      Long cityId = cityService.getCityId(message.getData().getCity());

      // Преобразование Message и cityId в UserDto с помощью маппера
      UserDto userDto = messageToUserMapper.toUserDto(message, cityId);

      // Обработка и отправка запроса на обновление
      userService.processAndUpdateUser(userDto);

    } catch (Exception e) {
      // Сохранение сбойного сообщения
      saveProblematicMessage(record.value());
      System.err.println("Ошибка при обработке сообщения: " + e.getMessage());
    }
  }

  /**
   * Сохраняет проблемное сообщение в базу данных.
   *
   * @param userDto объект userDto
   */
  private void saveProblematicMessage(UserDto userDto) {
    try {

      String jsonContent = objectMapper.writeValueAsString(userDto);

      // Сохранение JSON в базу данных
      ProblemMessage problemMessage = new ProblemMessage();
      problemMessage.setMessageContent(jsonContent);
      problemMessage.setAttempts(0);
      problemMessageRepository.save(problemMessage);
    } catch (Exception e) {
      // Здесь может быть дополнительное логирование или уведомление об ошибке
      System.err.println("Ошибка при сохранении проблемного сообщения: " + e.getMessage());
    }
  }
}
