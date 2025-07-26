package com.course.platform.model;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
public class UserActivityProjection {
    private String userName;
    private String email;
    private String role;
    private String action;
    private String platform;
    private Date timestamp;

    public UserActivityProjection(String userName, String email, String role, String action, String platform, Date timestamp) {
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.action = action;
        this.platform = platform;
        this.timestamp = timestamp;
    }

    // Getters and Setters
}
