package com.course.platform.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseVo {
    private String title;
    private Long courseId;
    private String courseDescription;
}