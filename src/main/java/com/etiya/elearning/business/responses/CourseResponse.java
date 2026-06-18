package com.etiya.elearning.business.responses;

import com.etiya.elearning.entities.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private Long categoryId;
    private Long instructorId;
    private Long languageId;
    private String courseName;
    private BigDecimal price;
    private String description;
    private Difficulty difficulty;
}
