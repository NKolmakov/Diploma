package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Answer;
import com.ggkttd.kolmakov.testSystem.domain.AnswerLog;
import com.ggkttd.kolmakov.testSystem.domain.Question;
import com.ggkttd.kolmakov.testSystem.repo.AnswerLogRepo;
import com.ggkttd.kolmakov.testSystem.services.AnswerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AnswerLogServiceImpl implements AnswerLogService {
    @Autowired
    private AnswerLogRepo answerLogRepo;
    @Override
    public AnswerLog save(AnswerLog answerLog) {
        validate(answerLog);
        return answerLogRepo.save(answerLog);
    }

    private void validate(AnswerLog answerLog){
        for (Question question:answerLog.getQuestions()){
            if(question.getAnswerLog() == null){
                question.setAnswerLog(answerLog);
            }
            for (Answer answer:question.getAnswers()){
                if(answer.getAnswerLog() == null){
                    answer.setAnswerLog(answerLog);
                }
            }
        }
    }
}
