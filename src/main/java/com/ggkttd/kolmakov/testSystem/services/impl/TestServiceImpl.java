package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Answer;
import com.ggkttd.kolmakov.testSystem.domain.Question;
import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
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
    public List<Test> findAll() {
        return testRepo.findAll();
    }

    @Override
    public Test getOne(Long id) {
        return testRepo.findById(id).orElseThrow(() -> new NotFoundException("TEST NOT FOUND"));
    }

    @Override
    public List<Test> getTestsBySubjectName(String name) {
        return testRepo.getTestsBySubjectName(name);
    }

    @Override
    public Test save(Test test) {
        validateTest(test);
        return testRepo.save(test);
    }

    @Override
    public void delete(Test test) {
        testRepo.delete(test);
    }

    private void validateTest(Test test) {
        for (Question question : test.getQuestions()) {
            if (question.getTest() == null) {
                question.setTest(test);
            }
            for (Answer answer:question.getAnswers()){
                if(answer.getQuestion() == null){
                    answer.setQuestion(question);
                }
            }
        }
    }
}
