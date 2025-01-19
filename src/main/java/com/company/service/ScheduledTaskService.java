package com.company.service;

import com.company.entity.ProblemMessage;
import com.company.repository.ProblemMessageRepository;
import com.company.service.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для обработки запланированных задач.
 */
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

  private final ProblemMessageRepository problemMessageRepository;
  private final UserService userService;
  private final ObjectMapper objectMapper; // Используем Jackson для работы с JSON

  /**
   * Обработка проблемных сообщений.
   */
  @Scheduled(fixedDelayString = "${scheduled.task.delay}", initialDelayString = "${scheduled.task.initialDelay}")
  @SchedulerLock(name = "processProblemMessages", lockAtLeastFor = 10, lockAtMostFor = 10)
  @Transactional
  public void processProblemMessages() {
    List<ProblemMessage> messages = problemMessageRepository.findTopByTimestampAndAttempts(5);

    for (ProblemMessage message : messages) {
      try {
        // Преобразование JSON в UserDto объект
        UserDto userDto = objectMapper.readValue(message.getMessageContent(), UserDto.class);

        // Обработка и отправка запроса на обновление
        userService.processAndUpdateUser(userDto);

        // Удаляем успешно обработанное сообщение
        problemMessageRepository.delete(message);

      } catch (Exception e) {
        // Логирование ошибки
        System.err.println("Ошибка при обработке сообщения с ID: " + message.getId() + ". " + e.getMessage());
        // Обновление числа попыток
        message.setAttempts(message.getAttempts() + 1);
        problemMessageRepository.save(message);
      }
    }
  }

}
