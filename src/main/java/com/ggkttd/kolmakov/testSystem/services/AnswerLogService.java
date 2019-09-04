package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.AnswerLog;

import java.util.List;

public interface AnswerLogService {
    AnswerLog save(AnswerLog answerLog);
    List<AnswerLog> saveAll(List<AnswerLog> answerLogs);
    List<AnswerLog> getByPassingTestId(Long id);
}
