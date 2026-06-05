package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessions, Integer> {
    Optional<UserSessions> findByToken(String token);

    void deleteByUser(User user);

    @Modifying
    @Query("UPDATE user_sessions us SET us.revoked = true WHERE us.user = :user")
    void revokeAllByUser(User user);
}
