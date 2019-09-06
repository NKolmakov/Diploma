package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Role;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ggkttd.kolmakov.testSystem.repo.RoleRepo;
import com.ggkttd.kolmakov.testSystem.repo.UserRepo;
import com.ggkttd.kolmakov.testSystem.services.UserService;
import com.ggkttd.kolmakov.testSystem.utils.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("USER NOT FOUND"));
    }

    @Override
    public User save(User user) {
        String encodedPassword = encoder.encode(user.getPassword());
        Role roleFromDb = roleRepo.getRoleByName(UserRoles.STUDENT.toString());
        //todo: MOCK
        if (roleFromDb == null) {
            user.setRole(new Role(UserRoles.STUDENT));
        } else {
            user.setRole(roleFromDb);
        }
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public User checkAuthorization(User user) {
        User userFromDb = userRepo.getUserByLogin(user.getLogin());
        if (userFromDb != null && encoder.matches(user.getPassword(), userFromDb.getPassword())) {
            userFromDb.setAuthorized(true);
            return userFromDb;
        }
        return user;
    }

    @Override
    public User findByLogin(User user) {
        return userRepo.getUserByLogin(user.getLogin());
    }
}
