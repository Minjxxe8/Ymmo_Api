package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.dto.UserAuthDto;
import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.model.UserSessions;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UserSessionRepository;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;


@Service
public class AuthService {

    private final UsersRepository usersRepository;
    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final UserSessionRepository userSessionRepository;

    @Autowired
    public AuthService(UsersRepository usersRepository, UserSessionRepository userSessionRepository, UserService userService, JWTUtils jwtUtils) {
        this.usersRepository = usersRepository;
        this.userService = userService;
        this.userSessionRepository = userSessionRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public UserAuthDto.AuthResponse register(UserCreationDto req) {

        if (usersRepository.existsUsersByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use : " + req.getEmail());
        }

        ResponseEntity<Users> response = userService.createUser(req);
        return issueTokenPair(response.getBody());
    }


    @Transactional
    public UserAuthDto.AuthResponse login(UserAuthDto.LoginRequest req) {
        if (usersRepository.existsUsersByEmail(req.email())) {
            PasswordService passwordService = new PasswordService();
            Users user = usersRepository.findByEmail(req.email());
            if (passwordService.verifyPassword(req.password(), user.getPassword())) {
                return issueTokenPair(user);
            }
        }
        return null;
    }


    @Transactional
    public UserAuthDto.AuthResponse refresh(UserAuthDto.RefreshRequest req) {
        String rawToken = req.refreshToken();

        UserSessions stored = userSessionRepository.findByToken(rawToken)
                .orElseThrow(() -> new IllegalArgumentException("Unknown refresh token"));

        if (stored.isRevoked()) {
            throw new IllegalArgumentException("Already revoked token");
        }
        if (stored.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            throw new IllegalArgumentException("Already expired token");
        }

        Users user = stored.getUser();

        if (!jwtUtils.validateRefreshToken(rawToken, user)) {
            throw new IllegalArgumentException("Invalid token hash");
        }

        stored.setRevoked(true);
        userSessionRepository.save(stored);

        return issueTokenPair(user);
    }


    @Transactional
    public void logout(UserAuthDto.RefreshRequest req) {
        userSessionRepository.findByToken(req.refreshToken())
                .ifPresent(userSessions -> {
                    userSessions.setRevoked(true);
                    userSessionRepository.save(userSessions);
                });
    }

    @Transactional
    public void logoutAll(UserAuthDto.RefreshRequest refreshRequest) {
        userSessionRepository.revokeAllByUser(
                usersRepository.findByEmail(
                        jwtUtils.extractUsernameFromRefreshToken(refreshRequest.refreshToken())));
    }


    private UserAuthDto.AuthResponse issueTokenPair(Users user) {
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        Timestamp now = Timestamp.from(Instant.now());

        UserSessions entity = new UserSessions(user, false, refreshToken, now, new Timestamp(now.getTime() + jwtUtils.getJwtRefreshExpiration()));
        userSessionRepository.save(entity);

        return UserAuthDto.AuthResponse.of(accessToken, refreshToken,
                jwtUtils.getJwtAccessExpiration(),
                jwtUtils.getJwtRefreshExpiration());
    }
}
