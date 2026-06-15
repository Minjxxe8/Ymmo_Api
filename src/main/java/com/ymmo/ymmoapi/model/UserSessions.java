package com.ymmo.ymmoapi.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "user_sessions")
public class UserSessions {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "revoked")
    private boolean revoked;

    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    public UserSessions() {

    }

    public int getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public String getToken() {
        return token;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public UserSessions(Users user, boolean revoked, String token, Timestamp createdAt, Timestamp expiresAt) {
        this.user = user;
        this.revoked = revoked;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
