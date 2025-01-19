package com.company.repository;

import com.company.entity.ErrorMessageData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Репозиторий для работы с проблемными сообщениями из Kafka.
 */
@Repository
public interface KafkaProblematicMessageRepository extends CrudRepository<ErrorMessageData, Long> {

  /**
   * Находит до 5 записей с попытками меньше 5, отсортированные по временной метке по убыванию.
   *
   * @return список проблемных сообщений
   */
  @Query("SELECT * FROM kafka_problematic_messages WHERE attempt_count < 5 ORDER BY timestamp DESC LIMIT 5")
  List<ErrorMessageData> findTopByTimestampWithAttemptsLessThanFive();
}
