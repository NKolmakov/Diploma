package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.Test;

import java.util.List;

public interface TestService {
    List<Test> findAll();
    Test getOne(Long id);
    List<Test> getTestsBySubjectName(String name);
    Test save(Test test);
    void delete(Test test);
}
