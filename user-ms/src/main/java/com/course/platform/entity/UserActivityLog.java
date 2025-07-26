package com.course.platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "user_activity_log")
@Getter
@Setter
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String action;
    private String ipAddress;
    private String platform;
    private Integer statusCode;
    private Long referenceId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    // Constructors, Getters, Setters
}
