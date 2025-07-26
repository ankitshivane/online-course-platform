package com.course.platform.service;

import com.course.platform.entity.User;
import com.course.platform.model.SearchRequest;
import com.course.platform.model.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(UserDto userDto);
    void deleteUser(Long id);
    Page<User> searchUser(SearchRequest userDto);
}
