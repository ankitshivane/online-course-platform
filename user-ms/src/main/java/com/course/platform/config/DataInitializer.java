package com.course.platform.config;

import com.course.platform.entity.Role;
import com.course.platform.entity.User;
import com.course.platform.entity.UserActivityLog;
import com.course.platform.repository.RoleRepository;
import com.course.platform.repository.UserActivityLogRepository;
import com.course.platform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadData(UserRepository userRepository, RoleRepository roleRepository,
                               UserActivityLogRepository logRepo) {
        return args -> {
            Role admin = new Role();
            admin.setName("ADMIN");

            Role student = new Role();
            student.setName("STUDENT");

            roleRepository.saveAll(List.of(admin, student));

            User user1 = new User();
            user1.setName("Ankit");
            user1.setEmail("ankit@example.com");
            user1.setRoles(Set.of(admin));

            User user2 = new User();
            user2.setName("Adwitya");
            user2.setEmail("adwityadube@example.com");
            user2.setRoles(Set.of(student));

            userRepository.saveAll(List.of(user1, user2));

            // Add activity logs
            UserActivityLog log1 = new UserActivityLog();
            log1.setUser(user1);
            log1.setAction("LOGIN");
            log1.setIpAddress("192.168.1.10");
            log1.setPlatform("WEB");
            log1.setStatusCode(200);
            log1.setReferenceId(1001L);
            log1.setTimestamp(new Date());

            UserActivityLog log2 = new UserActivityLog();
            log2.setUser(user2);
            log2.setAction("LOGOUT");
            log2.setIpAddress("10.0.0.5");
            log2.setPlatform("ANDROID");
            log2.setStatusCode(200);
            log2.setReferenceId(1002L);
            log2.setTimestamp(new Date());

            logRepo.saveAll(List.of(log1, log2));
        };
    }
}
