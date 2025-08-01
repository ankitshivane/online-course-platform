package com.course.platform.controller;

import com.course.platform.entity.Course;
import com.course.platform.model.CourseVo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/courses")
@Slf4j
public class CourseController {

/*
    @GetMapping
    public List<CourseVo> getAllCourses() {
        return List.of(CourseVo.builder().title("html").courseDescription("Html tutorial").courseId(1L).build());
    }*/
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

    @Operation(summary = "Get all courses")
    @GetMapping
    public List<Map<String, Object>> getAllCourses() {
        return List.of(
                Map.of("id", 1, "title", "Java Basics"),
                Map.of("id", 2, "title", "Spring Boot Advanced")
        );
    }

    @Operation(summary = "Get courses by user (dummy example)")
    @GetMapping("/user/{userId}")
//    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "fallbackCoursesForUser")
    public List<Map<String, Object>> getCoursesForUser(@PathVariable Long userId) {
        log.info("Inside course api for id:{}",userId);
        // Simulate a potential failure or delay to demonstrate circuit breaker
        if (userId == 0) {
            throw new RuntimeException("User not found!");
        }
        return List.of(
                Map.of("id", 10, "title", "Microservices with Spring", "userId", userId)
        );
    }

    public List<Map<String, Object>> fallbackCoursesForUser(Long userId, Throwable throwable) {
        return List.of(Map.of(
                "message", "Could not fetch user courses right now. Please try again later.",
                "userId", userId
        ));
    }
}
