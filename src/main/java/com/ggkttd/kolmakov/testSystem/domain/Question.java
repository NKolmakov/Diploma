package com.ggkttd.kolmakov.testSystem.domain;

import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
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

    @Transient
    private boolean manyRightAnswers;

    @Transient
    private boolean answeredRight;

    public Answer getById(Long id){
        for (Answer answer: answers){
            if(answer.getId().equals(id)){
                return answer;
            }
        }

        throw new NotFoundException("ANSWER #"+id+" NOT FOUND");
    }
}
