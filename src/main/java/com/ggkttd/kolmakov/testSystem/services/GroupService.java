package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.Group;

import java.util.List;

public interface GroupService {
    List<Group> getAll();
    Group getGroup(Long id);
    Group save(Group group);
    void delete(Group group);
    Group getGroupByName(String name);
}
