package com.company.service;

import com.company.dto.UserBusinessDto;
import com.company.entity.KafkaProblematicMessage;
import com.company.repository.KafkaProblematicMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledUserUpdateTask {

  private final KafkaProblematicMessageRepository messageRepository;
  private final UserService userService;
  private final ObjectMapper objectMapper;

  /**
   * Запланированное задание для обработки проблемных сообщений.
   */
  @Scheduled(fixedRate = 60000)  // Запускается каждую минуту
  @SchedulerLock(name = "userUpdateTask", lockAtLeastFor = "PT1M", lockAtMostFor = "PT5M")
  @Transactional
  public void processProblematicMessages() {
    List<KafkaProblematicMessage> messages = messageRepository.findTopByTimestampWithAttemptsLessThanFive();
    for (KafkaProblematicMessage msg : messages) {
      try {
        // Преобразуем и отправляем обновление пользователя
        UserBusinessDto userBusinessDto = objectMapper.readValue(msg.getMessage(), UserBusinessDto.class);

        userService.updateUser(userBusinessDto);

        // Удаляем сообщение после успешной обработки
        messageRepository.deleteById(msg.getId());
      } catch (Exception e) {
        log.error("Ошибка при обработке сообщения с id {}: {}", msg.getId(), e.getMessage(), e);

        // Обновляем количество попыток
        msg.setAttemptCount(msg.getAttemptCount() + 1);
        messageRepository.save(msg);
      }
    }
  }
}
