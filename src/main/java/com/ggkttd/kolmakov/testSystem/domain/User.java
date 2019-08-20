package com.ggkttd.kolmakov.testSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20,message = "Invalid user name length")
    private String name;

    @NotBlank
    @Size(max = 20, message = "Invalid user surname length")
    private String surname;

    @NotBlank
    @Size(max = 30,message = "Invalid user login length")
    private String login;

    @NotBlank
    @Size(max = 60,message = "Invalid user password length")
    private String password;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_group_id")
    private Group group;

    @Transient
    private boolean isAuthorized;

    public User(
            @NotBlank @Size(max = 20, message = "Invalid user name length") String name,
            @NotBlank @Size(max = 20, message = "Invalid user surname length") String surname,
            @NotBlank @Size(max = 30, message = "Invalid user login length") String login,
            @NotBlank @Size(max = 50, message = "Invalid user password length") String password,
            Role role,
            Group group,
            boolean isAuthorized) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.role = role;
        this.group = group;
        this.isAuthorized = isAuthorized;
    }

    public User(
            @NotBlank @Size(max = 20, message = "Invalid user name length") String name,
            @NotBlank @Size(max = 20, message = "Invalid user surname length") String surname,
            @NotBlank @Size(max = 30, message = "Invalid user login length") String login,
            @NotBlank @Size(max = 50, message = "Invalid user password length") String password,
            Group group) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.group = group;
    }
}
