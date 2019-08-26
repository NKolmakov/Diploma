package com.ggkttd.kolmakov.testSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "test")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255,message = "Invalid question name length")
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id")
    private Test test;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Answer> answers;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id")
    private AnswerLog answerLog;

    @Transient
    private boolean manyRightAnswers;
}
