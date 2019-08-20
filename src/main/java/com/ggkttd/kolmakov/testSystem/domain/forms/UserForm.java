package com.ggkttd.kolmakov.testSystem.domain.forms;

import lombok.Data;

@Data
public class UserForm {
    private String name;
    private String surname;
    private String login;
    private String password;
    private Long groupId;
    private Long roleId;

}
