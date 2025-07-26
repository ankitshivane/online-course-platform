package com.course.platform.controller;

import com.course.platform.model.SearchRequest;
import com.course.platform.model.UserActivityProjection;
import com.course.platform.service.UserProjectionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search/user-projection")
public class UserActivityProjectionController {

    @Autowired
    private UserProjectionSearchService projectionSearchService;

    @PostMapping("/project")
    public Page<UserActivityProjection> search(
            @RequestBody SearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectionSearchService.searchUserActivity(request, pageable);
    }
}
