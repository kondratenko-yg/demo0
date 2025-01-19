package com.company.service;

import com.company.UserData;
import com.company.entity.ErrorMessageData;
import com.company.mapper.MessageToUserDtoMapper;
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


  private final ProcessService processService;
  private final MessageToUserDtoMapper mapper;
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
      processService.process(message.getData());

    } catch (Exception e) {
      log.error("Ошибка при обработке сообщения из Kafka: {}", e.getMessage(), e);
      saveFailedMessage(message.getData(), e.toString());
    }
  }

  private void saveFailedMessage(UserData data, String errorMessage) {
    try {
      // Сохранение проблемного сообщения
      ErrorMessageData failedMessage = new ErrorMessageData();
      failedMessage.setMessage(objectMapper.writeValueAsString(data));
      failedMessage.setErrorMessage(errorMessage);
      failedMessage.setTimestamp(data.getTimestamp());

      messageRepository.save(failedMessage);
    } catch (Exception e) {
      log.error("Ошибка при сохранении проблемного сообщения.", e);
    }
  }
}