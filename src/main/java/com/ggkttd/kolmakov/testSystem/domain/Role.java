package com.ggkttd.kolmakov.testSystem.domain;

import com.ggkttd.kolmakov.testSystem.utils.UserRoles;
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
    @Enumerated(value = EnumType.STRING)
    private UserRoles name;

}
