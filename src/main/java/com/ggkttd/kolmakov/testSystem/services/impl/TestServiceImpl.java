package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.repo.TestRepo;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TestServiceImpl implements TestService {
    @Autowired
    private TestRepo testRepo;

    @Override
    public List<Test> getTestsBySubjectName(String name) {
        return testRepo.getTestsBySubjectName(name);
    }

    @Override
    public Test save(Test test) {
        return testRepo.save(test);
    }
}
