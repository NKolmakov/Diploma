package com.ggkttd.kolmakov.testSystem.domain.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatisticForm {
    private Integer passed;
    private Integer left2Pass;
    private Integer averageTime;
    private Integer percentageCorrect;
    private String goodPerformance;
    private String lowestPerformance;
    private Integer rating;
}
