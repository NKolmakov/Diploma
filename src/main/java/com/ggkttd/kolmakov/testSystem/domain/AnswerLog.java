package com.ggkttd.kolmakov.testSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "answer_log")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "passing_test_id")
    private PassingTest passingTest;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @Column(name = "is_right")
    private boolean isRight;


    public AnswerLog(PassingTest passingTest, Question question) {
        this.passingTest = passingTest;
        this.question = question;
    }

    public AnswerLog(PassingTest passingTest, Question question, Answer answer) {
        this.passingTest = passingTest;
        this.question = question;
        this.answer = answer;
    }

    public AnswerLog(PassingTest passingTest, Question question, Answer answer, boolean isRight) {
        this.passingTest = passingTest;
        this.question = question;
        this.answer = answer;
        this.isRight = isRight;
    }
}
