package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.Test;

import java.util.List;

public interface TestService {
    List<Test> getTestsBySubjectName(String name);
    Test save(Test test);
}
