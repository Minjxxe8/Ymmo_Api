package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.UserAuthDto;
import com.ymmo.ymmoapi.dto.UserCreationDto;
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
    public ResponseEntity<String> Login(@RequestBody UserAuthDto.LoginRequest loginRequest) {
        return ResponseEntity.ok("");
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthDto.AuthResponse> Register(@RequestBody UserCreationDto userCreationDto) {
        return ResponseEntity.ok(authService.register(userCreationDto));
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
    public ResponseEntity<String> Logout(@RequestBody UserAuthDto.RefreshRequest refreshRequest) {
        authService.logout(refreshRequest);
        return ResponseEntity.ok("Logged out");
    }
}
