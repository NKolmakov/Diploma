package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUser(Long id);
    User save(User user);
    void delete(User user);
    User checkAuthorization(User user);
    User findByLogin(User user);
}
