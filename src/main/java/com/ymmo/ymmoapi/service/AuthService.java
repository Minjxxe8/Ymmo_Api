package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.dto.UserAuthDto;
import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.model.UserSessions;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UserSessionRepository;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UsersRepository usersRepository,
                       UserSessionRepository userSessionRepository,
                       UserService userService,
                       JWTUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.usersRepository = usersRepository;
        this.userService = userService;
        this.userSessionRepository = userSessionRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserAuthDto.AuthResponse register(UserCreationDto req) {
        if (req.getEmail() == null || req.getUnhashedPassword() == null) {
            throw new ResponseException("Values cannot be null", 400);
        }

        if (!userService.isEmailCorrect(req.getEmail())) {
            System.out.println("Incorrect Email");
            throw new ResponseException("Incorrect Email", 400);
        }

        if (!userService.isPasswordCorrect(req.getUnhashedPassword())) {
            throw new ResponseException("Password too short (at least 8 characters)", 400);
        }


        if (usersRepository.existsUsersByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use : " + req.getEmail());
        }

        Users response = userService.createUser(req);
        return issueTokenPair(response);
    }


    @Transactional
    public UserAuthDto.AuthResponse login(UserAuthDto.LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.password())
            );
        } catch (BadCredentialsException e) {
            throw new ResponseException("Wrong credentials", 401);
        }

        Users user = usersRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseException("User not found", 404));

        return issueTokenPair(user);
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
