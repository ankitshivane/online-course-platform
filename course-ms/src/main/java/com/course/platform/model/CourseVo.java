package com.course.platform.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class CourseVo {
    private String title;
    private Long courseId;
    private String courseDescription;
}