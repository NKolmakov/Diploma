package com.ggkttd.kolmakov.testSystem.repo;

import com.ggkttd.kolmakov.testSystem.domain.AnswerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerLogRepo extends JpaRepository<AnswerLog,Long> {
    @Query(value = "select * from answer_log a where a.passing_test_id = :id",nativeQuery = true)
    List<AnswerLog> getLogsByPassingTestId(@Param("id") Long id);
}
