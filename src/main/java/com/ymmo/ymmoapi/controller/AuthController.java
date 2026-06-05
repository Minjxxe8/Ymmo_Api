package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.UserAuthDto;
import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthDto.AuthResponse> Login(@RequestBody UserAuthDto.LoginRequest loginRequest) {
        UserAuthDto.AuthResponse response = authService.login(loginRequest);
        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthDto.AuthResponse> Register(@RequestBody UserCreationDto userCreationDto) {
        try {
            return ResponseEntity.ok(authService.register(userCreationDto));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserAuthDto.AuthResponse> Refresh(@RequestBody UserAuthDto.RefreshRequest refreshRequest) {
        try {
            return ResponseEntity.ok(authService.refresh(refreshRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> Logout(@RequestBody UserAuthDto.LogoutAll logoutAll) {
        if (logoutAll.revokeAll()) {
            authService.logoutAll(new UserAuthDto.RefreshRequest(logoutAll.refreshToken()));
        } else {
            authService.logout(new UserAuthDto.RefreshRequest(logoutAll.refreshToken()));
        }
        return ResponseEntity.ok("Logged out");
    }
}
