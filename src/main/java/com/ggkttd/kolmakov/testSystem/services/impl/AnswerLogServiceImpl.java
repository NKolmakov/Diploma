package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.AnswerLog;
import com.ggkttd.kolmakov.testSystem.repo.AnswerLogRepo;
import com.ggkttd.kolmakov.testSystem.services.AnswerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AnswerLogServiceImpl implements AnswerLogService {
    @Autowired
    private AnswerLogRepo answerLogRepo;

    @Override
    public AnswerLog save(AnswerLog answerLog) {
        return answerLogRepo.save(answerLog);
    }

    @Override
    public List<AnswerLog> saveAll(List<AnswerLog> answerLogs) {
        return answerLogRepo.saveAll(answerLogs);
    }

    @Override
    public List<AnswerLog> getByPassingTestId(Long id) {
        return answerLogRepo.getLogsByPassingTestId(id);
    }
}
