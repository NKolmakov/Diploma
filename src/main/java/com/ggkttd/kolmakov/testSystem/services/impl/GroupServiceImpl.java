package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Group;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ggkttd.kolmakov.testSystem.repo.GroupRepo;
import com.ggkttd.kolmakov.testSystem.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepo groupRepo;

    @Override
    public List<Group> getAll() {
        return groupRepo.findAll();
    }

    @Override
    public Group getGroup(Long id) {
        return groupRepo.findById(id).orElseThrow(() -> new NotFoundException("GROUP NOT FOUND"));
    }

    @Override
    public Group save(Group group) {
        return null;
    }

    @Override
    public void delete(Group group) {

    }

    @Override
    public Group getGroupByName(String name) {
        Group group = groupRepo.getGroupByName(name);
        if (group == null) {
            throw new NotFoundException("GROUP NOT FOUND");
        }
        return group;
    }
}
