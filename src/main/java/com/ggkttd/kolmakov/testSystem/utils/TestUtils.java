package com.ggkttd.kolmakov.testSystem.utils;

import com.ggkttd.kolmakov.testSystem.domain.Answer;
import com.ggkttd.kolmakov.testSystem.domain.AnswerLog;
import com.ggkttd.kolmakov.testSystem.domain.Question;
import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class TestUtils {

    public List<Question> getResultFromUserAnswers(Test test, List<AnswerLog> logsFromDb) {
        List<Question> questions = new LinkedList<>();

        for (Question question : test.getQuestions()) {
            boolean right = true;
            for (AnswerLog answerLog : getLogsByQuestion(logsFromDb, question)) {
                if (!answerLog.isRight()) {
                    right = false;
                    break;
                }
            }
            question.setAnsweredRight(right);
            questions.add(question);
        }
        return questions;
    }

    /**
     * Method to get all logs which contains info about current question
     *
     * @param logs     - all test logs, contains info about all questions
     * @param question - required question
     * @return - logs with required question info, or empty list if logs not found
     */
    public List<AnswerLog> getLogsByQuestion(List<AnswerLog> logs, Question question) {
        Collections.sort(logs, (o1, o2) -> Math.toIntExact(o1.getQuestion().getId() - o2.getQuestion().getId()));
        List<AnswerLog> logList = new LinkedList<>();
        for (AnswerLog answerLog : logs) {
            if (answerLog.getQuestion().getId().equals(question.getId())) {
                logList.add(answerLog);
            }
        }
        return logList;
    }

    public Question getQuestionById(List<Question> questions, Long id) {
        for (Question question : questions) {
            if (question.getId().equals(id)) {
                return question;
            }
        }

        throw new NotFoundException("QUESTION #" + id + " NOT FOUND");
    }

    public Answer getAnswerById(List<Answer> answers, Long id) {
        for (Answer answer : answers) {
            if (answer.getId().equals(id)) {
                return answer;
            }
        }

        throw new NotFoundException("ANSWER #" + id + " NOT FOUND");
    }

    public File saveTest2File(Test test, String path) {
        String fileName = path + test.getOwner().getName() + " " + test.getOwner().getSurname() + " " + test.getName() + ".doc";
        try {
            File file = new File(fileName);
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convert2Utf8(String string){
       return new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

}
