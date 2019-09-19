package com.ggkttd.kolmakov.testSystem.domain.forms;

import com.ggkttd.kolmakov.testSystem.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatisticForm {
    private User user;
    private Integer passed;
    private Integer left2Pass;
    private Integer averageTime;
    private Integer percentageCorrect;
    private String goodPerformance;
    private String lowestPerformance;
    private Integer rating;
}
