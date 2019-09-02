package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.AnswerLog;
import com.ggkttd.kolmakov.testSystem.domain.PassingTest;

import java.util.List;

public interface PassingTestService {
    List<PassingTest> getAll();
    PassingTest getOne(Long id);
    PassingTest save(PassingTest passingTest);
    void delete(PassingTest passingTest);
}
