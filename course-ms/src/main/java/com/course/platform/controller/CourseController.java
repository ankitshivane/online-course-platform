package com.course.platform.controller;

import com.course.platform.entity.Course;
import com.course.platform.model.CourseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apis/courses")
public class CourseController {


    @GetMapping
    public List<CourseVo> getAllCourses() {
        return List.of(CourseVo.builder().title("html").courseDescription("Html tutorial").courseId(1L).build());
    }
    @GetMapping("/{id}")
    public List<CourseVo> getCourseById(@PathVariable(name = "id") Long courseId) {
        if(courseId==1L)
        return List.of(CourseVo.builder().title("html").courseDescription("Html tutorial").courseId(1L).build());
        else
            throw new RuntimeException("Testing for circuit breaker.. failing purposely");
    }

    @GetMapping("/review/{id}")
    public String getCourseReviewById(@PathVariable(name = "id") Long courseId) {
        if(courseId==1L)
            return "Good";
        else
            throw new RuntimeException("Testing for circuit breaker.. failing purposely");
    }
    }
