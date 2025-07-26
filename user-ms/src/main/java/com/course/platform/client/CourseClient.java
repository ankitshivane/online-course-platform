package com.course.platform.client;

import com.course.platform.model.CourseVo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CourseClient {

    private final RestTemplate restTemplate=new RestTemplate();

    @CircuitBreaker(name = "courseService",fallbackMethod = "getCourseFallback")
    public String getCourse(Long courseId){
        String url="http://localhost:9092/apis/courses/review/"+courseId;
        return restTemplate.getForObject(url, String.class);
    }
    public String getCourseFallback(Long courseId,Throwable t){
        return "Review not available at the moment. Please try later.";
    }
}
