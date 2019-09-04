package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.*;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ggkttd.kolmakov.testSystem.repo.AnswerLogRepo;
import com.ggkttd.kolmakov.testSystem.repo.PassingTestRepo;
import com.ggkttd.kolmakov.testSystem.repo.TestRepo;
import com.ggkttd.kolmakov.testSystem.services.PassingTestService;
import com.ggkttd.kolmakov.testSystem.utils.TestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class PassingTestServiceImpl implements PassingTestService {
    private static final Logger LOGGER = Logger.getLogger(PassingTestServiceImpl.class);
    @Autowired
    private PassingTestRepo passingTestRepo;
    @Autowired
    private AnswerLogRepo answerLogRepo;
    @Autowired
    private TestRepo testRepo;
    @Autowired
    private TestUtils testUtils;

    @Override
    public List<PassingTest> getAll() {
        return passingTestRepo.findAll();
    }

    @Override
    public PassingTest getOne(Long id) {
        return passingTestRepo.findById(id).orElseThrow(() -> new NotFoundException("PASSING TEST #" + id + " NOT FOUND"));
    }

    @Override
    public PassingTest save(PassingTest passingTest) {
        //check user test
        PassingTest passingTest2Save = getUserTestResult(passingTest);
        List<AnswerLog> logs = passingTest2Save.getLogs();
        //get existing test from db to save current passingTest
        passingTest2Save.setTest(testRepo.getOne(passingTest.getTest().getId()));

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());
            Date date = dateFormat.parse(currentDate);
            passingTest2Save.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PassingTest savedPassingTest = passingTestRepo.save(passingTest2Save);
        //logs have to be saved after passing test
        answerLogRepo.saveAll(logs);
        return savedPassingTest;
    }

    @Override
    public void delete(PassingTest passingTest) {
        passingTestRepo.delete(passingTest);
    }

    @Override
    public List<Question> getCheckedQuestions(PassingTest passingTestFromDb) {
        List<AnswerLog> logs = answerLogRepo.getLogsByPassingTestId(passingTestFromDb.getId());
        List<Question> questions = testUtils.getResultFromUserAnswers(passingTestFromDb.getTest(), logs);
        return questions;
    }

    private List<AnswerLog> getLogsFromUserTest(PassingTest passingTest) {
        Test testFromDb = testRepo.getOne(passingTest.getTest().getId());
        List<Question> questionsFromDb = testFromDb.getQuestions();
        List<Question> questionsFromUserTest = passingTest.getTest().getQuestions();
        List<AnswerLog> logs = new LinkedList<>();

        Collections.sort(questionsFromDb, (o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));
        Collections.sort(questionsFromUserTest, (o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));

        for (Question question : questionsFromDb) {
            List<Answer> answers = question.getAnswers();
            Collections.sort(answers, (o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));

            Question question2Compare = testUtils.getQuestionById(questionsFromUserTest, question.getId());
            List<Answer> userAnswers = question2Compare.getAnswers();
            Collections.sort(userAnswers, (o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));

            for (Answer answer : answers) {
                Answer answer2Compare = testUtils.getAnswerById(userAnswers, answer.getId());
                boolean allTrue = answer.isRight() && answer2Compare.isChecked();
                boolean allFalse = !answer.isRight() && !answer2Compare.isChecked();

                if (allTrue || allFalse) {
                    logs.add(new AnswerLog(passingTest, question, answer, true));
                } else {
                    logs.add(new AnswerLog(passingTest, question, answer, false));
                }
            }
        }

        return logs;
    }

    /**
     * Method check user test by logs, which contains all information about user answers.
     * Current mode assume, that only all right answers have to be checked.
     *
     * @param passingTest - test to be checked
     * @return filled entity <i>passingTest</i> with all user test info including data, time answers info
     */
    private PassingTest getUserTestResult(PassingTest passingTest) {
        List<AnswerLog> logs = getLogsFromUserTest(passingTest);
        int rightQuestions = 0;
        int falseQuestions = 0;
        for (Question question : passingTest.getTest().getQuestions()) {
            boolean allRight = true;
            List<AnswerLog> currentQuestionLogs = testUtils.getLogsByQuestion(logs, question);

            for (AnswerLog answerLog : currentQuestionLogs) {
                if (!answerLog.isRight()) {
                    allRight = false;
                    break;
                }
            }

            if (allRight) {
                rightQuestions++;
            } else {
                falseQuestions++;
            }
        }
        passingTest.setCommonQuestionsAmount((short) passingTest.getTest().getQuestions().size());
        passingTest.setCorrectQuestionsAmount((short) rightQuestions);
        passingTest.setUncorrectQuestionAmount((short) falseQuestions);
        passingTest.setLogs(logs);

        return passingTest;
    }

}
