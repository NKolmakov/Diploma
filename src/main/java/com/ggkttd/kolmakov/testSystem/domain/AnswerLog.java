package com.ggkttd.kolmakov.testSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "answer_log")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "answerLog",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Question> questions;

    @Transient
    private Long testId;
}
