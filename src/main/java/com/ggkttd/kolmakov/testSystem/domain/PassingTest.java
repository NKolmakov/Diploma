package com.ggkttd.kolmakov.testSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Table(name = "passing_test")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassingTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "test_id")
    private Test test;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;
    @JoinColumn(name = "user_id")
    private Long userId;

    private Date date;

    @Column(name = "start_time")
    private Time startTime;
    @Column(name = "end_time")
    private Time endTime;
    @Column(name = "common_question_amount")
    private short commonQuestionsAmount;
    @Column(name = "correct_question_amount")
    private short correctQuestionsAmount;
    @Column(name = "uncorrect_question_amount")
    private short uncorrectQuestionAmount;

    @Transient
    private List<AnswerLog> logs;
}
