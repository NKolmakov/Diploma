package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.domain.forms.StudentStatisticForm;

import java.util.List;

public interface TestService {
    Test getOne(Long id);
    Test sortQuestionsByDesc(Test test);
    List<Test> getNotPassed(Long userId,Long subjectId);
    List<Test> getByTutorId(Long tutorId);
    StudentStatisticForm getStatistic(User user);
    Test save(Test test);
    void delete(Test test);
}
