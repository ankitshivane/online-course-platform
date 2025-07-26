package com.course.platform.controller;

import com.course.platform.client.CourseClient;
import com.course.platform.entity.User;
import com.course.platform.exception.ResourceNotFoundException;
import com.course.platform.model.CourseVo;
import com.course.platform.model.SearchRequest;
import com.course.platform.model.UserDto;
import com.course.platform.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto created = userService.createUser(userDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<User>> searchUser(@RequestBody @Valid SearchRequest userDto) {
        Page<User> searchedUser = userService.searchUser(userDto);
        return new ResponseEntity<>(searchedUser, HttpStatus.CREATED);
    }

    @Autowired
    private CourseClient client;

    @GetMapping("/associate-course/{courseId}")
    public String getAssociatedCourse(@PathVariable(name = "courseId") Long courseId){
        return client.getCourse(courseId);
    }
}