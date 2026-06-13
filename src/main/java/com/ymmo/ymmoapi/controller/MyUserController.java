package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.MyUserModificationDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/users/me")
@CrossOrigin
public class MyUserController {
    private final UserService userService;

    public MyUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> myUserInfo() {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(userService.getUserByEmail(email));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PatchMapping()
    public ResponseEntity<?> updateMyUser(@RequestBody MyUserModificationDto myUserModificationDto) {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(userService.updateMyUser(email, myUserModificationDto));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
