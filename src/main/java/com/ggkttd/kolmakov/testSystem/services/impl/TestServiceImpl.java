package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Answer;
import com.ggkttd.kolmakov.testSystem.domain.Question;
import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.exceptions.InvalidTestException;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ggkttd.kolmakov.testSystem.repo.PassingTestRepo;
import com.ggkttd.kolmakov.testSystem.repo.TestRepo;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import com.ggkttd.kolmakov.testSystem.utils.TestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class TestServiceImpl implements TestService {
    private static final Logger LOGGER = Logger.getLogger(TestServiceImpl.class);

    @Autowired
    private TestRepo testRepo;

    @Autowired
    private PassingTestRepo passingTestRepo;

    @Autowired
    private TestUtils testUtils;

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

//    @Override
//    public StudentStatisticForm getStatistic(User user) {
//        StudentStatisticForm statistic = new StudentStatisticForm();
//        List<PassingTest> passingTests = passingTestRepo.getByUserId(user.getId());
//
//        statistic.setPassed(testRepo.getPassedTestsByUser(user.getId()).size());
//        statistic.setLeft2Pass(testRepo.getNotPassedTestsByUser(user.getId()).size());
//        statistic.setAverageTime(getAverageTime(passingTests));
//        statistic.setPercentageCorrect(getRightAnsPercentage(passingTests));
//        statistic.setGoodPerformance(getGoodPerformance(passingTests));
//        statistic.setLowestPerformance(getLowestPerformance(passingTests));
//        statistic.setRating(getRating(passingTests, user));
//        return statistic;
//    }

    @Override
    public Test save(Test test) {
        validateTest(test);
        saveMultipartFiles(test);
        return testRepo.save(test);
    }

    @Override
    public void delete(Test test) {
        testRepo.delete(test);
    }

    private void saveMultipartFiles(Test test) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String currentPath = test.getOwner().getLogin() + "\\" + formatter.format(date) + "\\";
        new File(currentPath).mkdirs();

        try {
            if (test.getFiles() != null) {
                for (MultipartFile file : test.getFiles()) {
                    List<Question> questions = getQuestions(test.getQuestions(), file.getOriginalFilename());
                    File savedFile = testUtils.saveMultipartFile(file, currentPath);
                    for (Question question : questions) {
                        question.getResource().setPath(currentPath);
                        question.getResource().setFileName(savedFile.getName());
                        question.getResource().setFileLength(savedFile.length());
                        question.getResource().setType(getFileType(savedFile));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.warn(e);
        }
    }

    private String getFileType(File file) {
        String type = null;

        try {
            type = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            LOGGER.warn(e);
        }

        return type;
    }

    private List<Question> getQuestions(List<Question> questions, String name) {
        List<Question> questionList = new LinkedList<>();

        for (Question question : questions) {
            if (question.getResource() != null &&
                    question.getResource().getFileName() != null &&
                    question.getResource().getFileName().equals(name)) {
                questionList.add(question);
            }
        }

        return questionList;
    }

//    private Integer getRating(List<PassingTest> passingTests, User user) {
//        List<StudentStatisticForm> statistics = new ArrayList<>(passingTests.size());
//        StudentStatisticForm form2Compare = null;
//        for (PassingTest test : passingTests) {
//            StudentStatisticForm form = new StudentStatisticForm();
//            form.setUser(test.getUser());
//            form.setPercentageCorrect(getRightAnsPercentage(passingTestRepo.getByUserId(test.getUser().getId())));
//            form.setAverageTime(getAverageTime(passingTestRepo.getByUserId(test.getUser().getId())));
//            statistics.add(form);
//            if (test.getUser().getId().equals(user.getId())) form2Compare = form;
//        }
//
//        statistics.sort(Comparator.comparing(StudentStatisticForm::getPercentageCorrect, (o1, o2) -> o2 - o1)
//                .thenComparing(StudentStatisticForm::getAverageTime));
//
//        computeRating(statistics);
//        return statistics.indexOf(form2Compare);
//    }
//
//    private void computeRating(List<StudentStatisticForm> userForms) {
//        userForms.sort(Comparator.comparing(StudentStatisticForm::getPercentageCorrect, (o1, o2) -> o2 - o1)
//                .thenComparing(StudentStatisticForm::getAverageTime));
//
//        for (int i = 0; i < userForms.size(); i++) {
//            if (i == 0) {
//                userForms.get(i).setRating(i + 1);
//            } else {
//                StudentStatisticForm previous = userForms.get(i - 1);
//                StudentStatisticForm current = userForms.get(i);
//
//                int previousRez = previous.getPercentageCorrect() + previous.getAverageTime();
//                int currentRez = current.getPercentageCorrect() + current.getAverageTime();
//
//                if (previousRez == currentRez) {
//                    userForms.get(i).setRating(previous.getRating());
//                } else {
//                    userForms.get(i).setRating(previous.getRating() + 1);
//                }
//
//            }
//        }
//    }

//    private String getLowestPerformance(List<PassingTest> passingTests) {
//        StringBuilder subject = new StringBuilder();
//        int badPercentage = 0;
//
//        for (PassingTest test : passingTests) {
//            int currentPercentage = test.getCorrectQuestionsAmount() * 100 / test.getCommonQuestionsAmount();
//            if (badPercentage > currentPercentage) {
//                subject = new StringBuilder(test.getTest().getSubject().getName());
//                badPercentage = currentPercentage;
//            } else if (badPercentage == currentPercentage) {
//                subject.append(", ");
//                subject.append(test.getTest().getSubject().getName());
//            }
//        }
//
//        return subject.toString();
//    }
//
//    private String getGoodPerformance(List<PassingTest> passingTests) {
//        StringBuilder subject = new StringBuilder();
//        int goodPercentage = 0;
//        for (PassingTest test : passingTests) {
//            int currentPercentage = test.getCorrectQuestionsAmount() * 100 / test.getCommonQuestionsAmount();
//            if (goodPercentage < currentPercentage) {
//                subject = new StringBuilder(test.getTest().getSubject().getName());
//                goodPercentage = currentPercentage;
//            } else if (goodPercentage != 0 && goodPercentage == currentPercentage) {
//                subject.append(", ");
//                subject.append(test.getTest().getSubject().getName());
//            }
//        }
//
//        return subject.toString();
//    }
//
//    private Integer getRightAnsPercentage(List<PassingTest> passingTests) {
//        int result = 0;
//        try {
//            int commonPercentage = 0;
//            for (PassingTest test : passingTests) {
//                commonPercentage += test.getCorrectQuestionsAmount() * 100 / test.getCommonQuestionsAmount();
//            }
//
//            if (passingTests.size() > 0) result = commonPercentage / passingTests.size();
//        } catch (Exception e) {
//            LOGGER.warn(e);
//        }
//        return result;
//    }
//
//    private Integer getAverageTime(List<PassingTest> passingTests) {
//        int result = 0;
//        try {
//            int commonTime = 0;
//            for (PassingTest test : passingTests) {
//                long startTime = test.getStartTime().getTime();
//                long endTime = test.getEndTime().getTime();
//                long difference = (endTime - startTime) / (1000 * 60);
//                commonTime += (int) difference;
//            }
//            if (passingTests.size() > 0) result = commonTime / passingTests.size();
//        } catch (Exception e) {
//            LOGGER.warn(e);
//        }
//
//        return result;
//    }

    private void validateTest(Test test) {
        if (test.getQuestions() != null) {
            for (Question question : test.getQuestions()) {
                if (question.getTest() == null) {
                    question.setTest(test);
                }
                if (question.getAnswers() != null) {
                    for (Answer answer : question.getAnswers()) {
                        if (answer.getQuestion() == null) {
                            answer.setQuestion(question);
                        }
                    }
                } else {
                    throw new InvalidTestException("QUESTION " + question.getName() + " DOESN'T CONTAIN ANY ANSWERS");
                }
            }
        } else {
            throw new InvalidTestException("TEST DOESN'T CONTAIN ANY QUESTIONS");
        }
    }
}
