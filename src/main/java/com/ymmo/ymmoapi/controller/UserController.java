package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.service.PasswordService;
import com.ymmo.ymmoapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UsersRepository usersRepository, PasswordService passwordService) {
        this.userService = new UserService(usersRepository, passwordService);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.status(204).body(e.toString());
        }
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Users> createUser(@RequestBody UserCreationDto user) {
        return ResponseEntity.ok(userService.createUser(user));
    }


    @PatchMapping("/users/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Users> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
