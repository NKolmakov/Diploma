package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.PassingTest;
import com.ggkttd.kolmakov.testSystem.domain.Question;

import java.util.List;

public interface PassingTestService {
    List<PassingTest> getAll();
    PassingTest getOne(Long id);
    PassingTest save(PassingTest passingTest);
    void delete(PassingTest passingTest);
    List<Question> getCheckedQuestions(PassingTest passingTestFromDb);
}
