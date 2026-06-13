package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.service.PasswordService;
import com.ymmo.ymmoapi.service.UserService;
import com.ymmo.ymmoapi.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UsersRepository usersRepository, PasswordService passwordService, WalletService walletService) {
        this.userService = new UserService(usersRepository, passwordService, walletService);
    }

    @GetMapping()
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.status(204).body(e.toString());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> createUser(@RequestBody UserCreationDto user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Users user) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }
}
