package com.course.platform.service;

import com.course.platform.client.CourseClient;
import com.course.platform.entity.Role;
import com.course.platform.entity.User;
import com.course.platform.exception.ResourceNotFoundException;
import com.course.platform.model.SearchRequest;
import com.course.platform.model.UserDto;
import com.course.platform.repository.RoleRepository;
import com.course.platform.repository.UserRepository;
import com.course.platform.specification.SearchSpecification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "User",key = "#id")
    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        Set<Role> roles = userDto.getRoles().stream()
                .map(roleName -> roleRepo.findAll().stream()
                        .filter(r -> r.getName().equalsIgnoreCase(roleName))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        User saved = userRepo.save(user);
        return mapToDto(saved);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepo.delete(user);
    }

    @Override
    public Page<User> searchUser(SearchRequest req) {
        PageRequest pageRequest = PageRequest.of(req.getPageNumber()-1, req.getPageSize());
        Page<User> all = userRepo.findAll(geMyUserBySearch(req), pageRequest);
        return all;
    }

    @Override
    public UserDto editUser(UserDto userDto) {
        User user = userRepo.findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDto.getId()));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        User updated = userRepo.save(user);
        return mapToDto(updated);
    }

    @Autowired
    private CourseClient client;

    @Override
//    @CircuitBreaker(name = "courseService",fallbackMethod = "getCourseFallback")
    public String makeCallToCourse(Long courseId) {
        return client.getCourse(courseId);
    }

    public String getCourseFallback(Long courseId,Throwable t){
        return "Review not available at the moment. Please try later.";
    }

    private Specification<User> geMyUserBySearch(SearchRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates=new ArrayList<>();
            SearchSpecification.searchByUserName(predicates,root,cb,req.getName());
            SearchSpecification.searchByEmail(predicates,root,cb,req.getEmail());
            SearchSpecification.searchByRole(predicates,root,cb,req);
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
        return dto;
    }
}
