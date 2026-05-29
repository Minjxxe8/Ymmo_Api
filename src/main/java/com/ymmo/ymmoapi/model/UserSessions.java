package com.ymmo.ymmoapi.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "user_sessions")
public class UserSessions {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "expires_at")
    private Timestamp expiresAt;
}
