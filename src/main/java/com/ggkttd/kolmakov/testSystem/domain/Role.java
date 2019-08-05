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
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20,message = "Invalid role name length")
    private String name;

    private boolean read;

    @Column(name = "pass_test")
    private boolean passTest;

    @Column(name = "create_user")
    private boolean createUser;

    @Column(name = "super_user")
    private boolean superUser;
}
