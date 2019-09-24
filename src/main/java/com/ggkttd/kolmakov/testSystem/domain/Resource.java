package com.ggkttd.kolmakov.testSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50,message = "Invalid type length")
    private String type;

    @Column(name = "file_name")
    @NotBlank
    @Size(max = 255,message = "Invalid file name length")
    private String fileName;

    @NotBlank
    @Size(max = 255,message = "Invalid path length")
    private String path;

    @Column(name = "file_length")
    @NotNull
    private Long fileLength;
}
