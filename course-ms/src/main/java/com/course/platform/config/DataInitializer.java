package com.course.platform.config;

import com.course.platform.entity.Course;
import com.course.platform.entity.Enrollment;
import com.course.platform.entity.Review;
import com.course.platform.repository.CourseRepository;
import com.course.platform.repository.EnrollmentRepository;
import com.course.platform.repository.ReviewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadData(
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository,
            ReviewRepository reviewRepository
    ) {
        return args -> {
            Course c1 = new Course();
            c1.setTitle("Java Fundamentals");
            c1.setDescription("Learn the basics of Java programming.");

            Course c2 = new Course();
            c2.setTitle("Spring Boot Mastery");
            c2.setDescription("Build REST APIs with Spring Boot.");

            courseRepository.saveAll(List.of(c1, c2));

            // Assume userId 1 and 2 exist in user-service
            Enrollment e1 = new Enrollment();
            e1.setUserId(1L);
            e1.setCourseId(c1.getId());

            Enrollment e2 = new Enrollment();
            e2.setUserId(2L);
            e2.setCourseId(c2.getId());

            enrollmentRepository.saveAll(List.of(e1, e2));

            Review r1 = new Review();
            r1.setUserId(1L);
            r1.setCourseId(c1.getId());
            r1.setRating(5);
            r1.setComment("Excellent intro to Java!");

            Review r2 = new Review();
            r2.setUserId(2L);
            r2.setCourseId(c2.getId());
            r2.setRating(4);
            r2.setComment("Very good, more examples needed.");

            reviewRepository.saveAll(List.of(r1, r2));
        };
    }
}
