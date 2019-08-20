package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Subject;
import com.ggkttd.kolmakov.testSystem.repo.SubjectRepo;
import com.ggkttd.kolmakov.testSystem.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectRepo subjectRepo;

    @Override
    public List<Subject> getAll() {
        return subjectRepo.findAll();
    }
}
