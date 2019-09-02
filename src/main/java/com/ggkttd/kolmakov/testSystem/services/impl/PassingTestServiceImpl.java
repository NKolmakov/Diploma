package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.*;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ggkttd.kolmakov.testSystem.repo.AnswerLogRepo;
import com.ggkttd.kolmakov.testSystem.repo.PassingTestRepo;
import com.ggkttd.kolmakov.testSystem.repo.TestRepo;
import com.ggkttd.kolmakov.testSystem.services.PassingTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class PassingTestServiceImpl implements PassingTestService {
    @Autowired
    private PassingTestRepo passingTestRepo;
    @Autowired
    private AnswerLogRepo answerLogRepo;
    @Autowired
    private TestRepo testRepo;

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
        PassingTest passingTest2Save = checkTestAllRightMode(passingTest);
        List<AnswerLog> logs = passingTest2Save.getLogs();
        passingTest2Save.setTest(testRepo.getOne(passingTest.getTest().getId()));
        PassingTest savedPassingTest = passingTestRepo.save(passingTest2Save);
        answerLogRepo.saveAll(logs);
        return savedPassingTest;
    }

    @Override
    public void delete(PassingTest passingTest) {
        passingTestRepo.delete(passingTest);
    }

    /**
     * Method check user test by logs, which contains all information about user answers.
     * Current mode assume, that only all right answers have to be checked.
     *
     * @param passingTest - test to be checked
     * @return filled entity <i>passingTest</i> with all user test info including data, time answers info
     */
    private PassingTest checkTestAllRightMode(PassingTest passingTest) {
        List<AnswerLog> logs = getLogsFromUserTest(passingTest);
        int rightQuestions = 0;
        int falseQuestions = 0;
        for (Question question : passingTest.getTest().getQuestions()) {
            boolean allRight = true;
            List<AnswerLog> currentQuestionLogs = getLogsByQuestion(logs, question);

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

    /**
     * Method compare user answers with original database answers to identify correct and uncorrected answers.
     *
     * @param passingTest - contains test from client
     * @return list of logs contains result of comparison with database test
     */
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

            Question question2Compare = getQuestionById(questionsFromUserTest, question.getId());
            List<Answer> userAnswers = question2Compare.getAnswers();
            Collections.sort(userAnswers, (o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));

            for (Answer answer : answers) {
                Answer answer2Compare = getAnswerById(userAnswers, answer.getId());
                if (answer.isRight() && answer2Compare.isChecked() || !answer.isRight() && !answer.isChecked()) {
//                if (!answer.isRight() && answer2Compare.isChecked()) {
                    logs.add(new AnswerLog(passingTest, question, answer, true));
                } else {
                    logs.add(new AnswerLog(passingTest, question, answer, false));
                }
            }
        }

        return logs;
    }

    /**
     * Method to get all logs which contains info about current question
     *
     * @param logs     - all test logs, contains info about all questions
     * @param question - required question
     * @return - logs with required question info, or empty list if logs not found
     */
    private List<AnswerLog> getLogsByQuestion(List<AnswerLog> logs, Question question) {
        Collections.sort(logs, (o1, o2) -> Math.toIntExact(o1.getQuestion().getId() - o2.getQuestion().getId()));
        List<AnswerLog> logList = new LinkedList<>();
        for (AnswerLog answerLog : logs) {
            if (answerLog.getQuestion().getId().equals(question.getId())) {
                logList.add(answerLog);
            }
        }
        return logList;
    }

    private Question getQuestionById(List<Question> questions, Long id) {
        for (Question question : questions) {
            if (question.getId().equals(id)) {
                return question;
            }
        }

        throw new NotFoundException("QUESTION #" + id + " NOT FOUND");
    }

    private Answer getAnswerById(List<Answer> answers, Long id) {
        for (Answer answer : answers) {
            if (answer.getId().equals(id)) {
                return answer;
            }
        }

        throw new NotFoundException("ANSWER #" + id + " NOT FOUND");
    }

}
