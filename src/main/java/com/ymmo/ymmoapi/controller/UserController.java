package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UsersRepository usersRepository) {
        this.userService = new UserService(usersRepository);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<Users> createUser(@RequestBody UserCreationDto user) {
        return userService.createUser(user);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
