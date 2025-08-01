package com.course.platform.controller;

import com.course.platform.client.CourseClient;
import com.course.platform.entity.User;
import com.course.platform.model.SearchRequest;
import com.course.platform.model.UserDto;
import com.course.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
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

    @Autowired
    private CourseClient client;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto created = userService.createUser(userDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CachePut(cacheNames = "User", key = "#id")
    public ResponseEntity<UserDto> editUser(@PathVariable Long id,@RequestBody UserDto userDto) {
        UserDto created = userService.editUser(userDto);
        return new ResponseEntity<>(created, HttpStatus.OK);
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



    @GetMapping("/associate-course/{courseId}")
    public String getAssociatedCourse(@PathVariable(name = "courseId") Long courseId){
        return userService.makeCallToCourse(courseId);
    }


    @Operation(summary = "Get courses for user by calling course-ms with circuit breaker")
    @GetMapping("/{id}/courses")
    public ResponseEntity<String> getUserCourses(Long id){
        log.info("Inside getUserCourses Controller: user Id:{}",id);
        return client.getUserCourses(id);
    }
}