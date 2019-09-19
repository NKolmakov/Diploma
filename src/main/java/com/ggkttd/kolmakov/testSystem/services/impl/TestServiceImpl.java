package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.*;
import com.ggkttd.kolmakov.testSystem.domain.forms.StudentStatisticForm;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ggkttd.kolmakov.testSystem.repo.PassingTestRepo;
import com.ggkttd.kolmakov.testSystem.repo.TestRepo;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class TestServiceImpl implements TestService {
    private static final Logger LOGGER = Logger.getLogger(TestServiceImpl.class);

    @Autowired
    private TestRepo testRepo;

    @Autowired
    private PassingTestRepo passingTestRepo;

    @Override
    public Test getOne(Long id) {
        Test test = testRepo.findById(id).orElseThrow(() -> new NotFoundException("TEST NOT FOUND"));

        //check every question to define right answers amount
        for (Question question : test.getQuestions()) {
            int rightAnsAmount = 0;

            for (Answer answer : question.getAnswers()) {
                if (answer.isRight()) {
                    rightAnsAmount++;
                    if (rightAnsAmount == 2) {
                        question.setManyRightAnswers(true);
                        break;
                    }
                }
            }
        }

        return test;
    }

    @Override
    public Test sortQuestionsByDesc(Test test) {
        List<Question> questions = test.getQuestions();
        Collections.sort(test.getQuestions(), (o1, o2) -> Math.toIntExact((o1.getId() - o2.getId()) * -1));
        return test;
    }

    @Override
    public List<Test> getNotPassed(Long userId, Long subjectId) {
        return testRepo.getNotPassedTestsBySubjectId(userId, subjectId);
    }

    @Override
    public List<Test> getByTutorId(Long tutorId) {
        return testRepo.getTestsByUserId(tutorId);
    }

    @Override
    public StudentStatisticForm getStatistic(User user) {
        StudentStatisticForm statistic = new StudentStatisticForm();
        List<PassingTest> passingTests = passingTestRepo.getByUserId(user.getId());

        statistic.setPassed(testRepo.getPassedTestsByUser(user.getId()).size());
        statistic.setLeft2Pass(testRepo.getNotPassedTestsByUser(user.getId()).size());
        statistic.setAverageTime(getAverageTime(passingTests));
        statistic.setPercentageCorrect(getRightAnsPercentage(passingTests));
        statistic.setGoodPerformance(getGoodPerformance(passingTests));
        return statistic;
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

    private String getGoodPerformance(List<PassingTest> passingTests){
        StringBuilder subject = new StringBuilder();
        int goodPercentage = 0;
        for (PassingTest test:passingTests){
            int currentPercentage = test.getCorrectQuestionsAmount()*100/test.getCommonQuestionsAmount();
            if(goodPercentage < currentPercentage){
                subject = new StringBuilder(test.getTest().getSubject().getName());
            }else if(goodPercentage == currentPercentage){
                subject.append(", ");
                subject.append(test.getTest().getSubject().getName());
            }
        }

        return subject.toString();
    }

    private Integer getRightAnsPercentage(List<PassingTest> passingTests){
        int result = 0;
            try {
                int commonPercentage = 0;
                for (PassingTest test:passingTests){
                    commonPercentage += test.getCorrectQuestionsAmount()*100/test.getCommonQuestionsAmount();
                }

                if(passingTests.size() > 0) result = commonPercentage/passingTests.size();
            }catch (Exception e){
                LOGGER.warn(e);
            }
        return result;
    }

    private Integer getAverageTime(List<PassingTest> passingTests) {
        int result = 0;
        try {
            int commonTime = 0;
            for (PassingTest test : passingTests) {
                long startTime = test.getStartTime().getTime();
                long endTime = test.getEndTime().getTime();
                long difference = (endTime - startTime) / (1000 * 60);
                commonTime += (int) difference;
            }
            if (passingTests.size() > 0) result = commonTime / passingTests.size();
        } catch (Exception e) {
            LOGGER.warn(e);
        }

        return result;
    }

    private void validateTest(Test test) {
        for (Question question : test.getQuestions()) {
            if (question.getTest() == null) {
                question.setTest(test);
            }
            for (Answer answer : question.getAnswers()) {
                if (answer.getQuestion() == null) {
                    answer.setQuestion(question);
                }
            }
        }
    }
}
