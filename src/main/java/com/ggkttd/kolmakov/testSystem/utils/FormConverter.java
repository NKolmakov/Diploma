package com.ggkttd.kolmakov.testSystem.utils;

import com.ggkttd.kolmakov.testSystem.domain.Group;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.domain.forms.UserForm;
import com.ggkttd.kolmakov.testSystem.repo.GroupRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormConverter {
    @Autowired
    private GroupRepo groupRepo;
    private static final Logger LOGGER = Logger.getLogger(FormConverter.class);

    public User convertUser(UserForm userForm){
        String name = userForm.getName();
        String surname = userForm.getSurname();
        String login = userForm.getLogin();
        String password = userForm.getPassword();
        Group group = null;
        if(userForm.getGroupId() != null){
            group = groupRepo.getOne(userForm.getGroupId());
        }

        return new User(name,surname,login,password,group);
    }
}
