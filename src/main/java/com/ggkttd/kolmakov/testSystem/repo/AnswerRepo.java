package com.ggkttd.kolmakov.testSystem.repo;


import com.ggkttd.kolmakov.testSystem.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepo extends JpaRepository<Answer,Long> {

}
