package com.company.service;

import com.company.dto.UserBusinessDto;
import com.company.entity.KafkaProblematicMessage;
import com.company.mapper.MessageToUserBusinessDtoMapper;
import com.company.repository.KafkaProblematicMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.company.Message;

/**
 * Сервис, слушающий сообщения из Kafka
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserMessageListener {

  private final CityService cityService;
  private final UserService userService;
  private final MessageToUserBusinessDtoMapper mapper;
  private final KafkaProblematicMessageRepository messageRepository;
  private final ObjectMapper objectMapper;

  /**
   * Принимает сообщение из Kafka и логирует его.
   *
   * @param record - потребленное сообщение.
   */
  @KafkaListener(topics = "user-updates", groupId = "user-service-group")
  public void listen(ConsumerRecord<String, Message> record) {
    Message message = record.value();
    try {
      // Получение идентификатора города
      Long cityId = cityService.getCityIdByName(message.getData().getCity().toString());

      // Преобразование Message в UserBusinessDto
      UserBusinessDto userBusinessDto = mapper.toUserBusinessDto(message, cityId);

      // Обновление пользователя
      userService.updateUser(userBusinessDto);

    } catch (Exception e) {
      log.error("Ошибка при обработке сообщения из Kafka: {}", e.getMessage(), e);
      saveFailedMessage(record, e.toString());
    }
  }

  private void saveFailedMessage(ConsumerRecord<String, Message> record, String errorMessage) {
    try {
      // Сохранение проблемного сообщения
      KafkaProblematicMessage failedMessage = new KafkaProblematicMessage();
      failedMessage.setKey(record.key());
      failedMessage.setMessage(objectMapper.writeValueAsString(record.value()));
      failedMessage.setErrorMessage(errorMessage);
      failedMessage.setAttemptCount(0);

      messageRepository.save(failedMessage);
    } catch (Exception e) {
      log.error("Ошибка при сохранении проблемного сообщения с ключом {}: {}", record.key(), e);
    }
  }
}