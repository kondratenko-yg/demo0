package com.company.repository;

import com.company.entity.ProblemMessage;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProblemMessageRepository extends CrudRepository<ProblemMessage, Long> {

  @Query("SELECT * FROM problem_messages WHERE attempts < :maxAttempts ORDER BY timestamp DESC")
  List<ProblemMessage> findTopByTimestampAndAttempts(int maxAttempts);
}
