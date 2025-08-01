package com.course.platform.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class CourseClient {

    final RestTemplate restTemplate;

    @Autowired
    public CourseClient(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    @CircuitBreaker(name = "courseServiceCB", fallbackMethod = "fallbackUserCourses")
    public ResponseEntity<String> getUserCourses(Long id){
        // Simulate call to course-ms
        String courseMsUrl = "http://localhost:9092/apis/courses/user/" + id;
        // This call might fail, circuit breaker manages it
        return restTemplate.getForEntity(courseMsUrl, String.class);
    }
    public ResponseEntity<?> fallbackUserCourses(Long id, Throwable throwable) {
        log.info("Inside the fallback of the the course:{}",id);
        return ResponseEntity.ok(Map.of(
                "message", "Course service unavailable. Showing fallback.",
                "userId", id,
                "courses", java.util.List.of()
        ));
    }

    public String getCourse(Long courseId){
        String url="http://localhost:9092/apis/courses/review/"+courseId;
        return restTemplate.getForObject(url, String.class);
    }

}
