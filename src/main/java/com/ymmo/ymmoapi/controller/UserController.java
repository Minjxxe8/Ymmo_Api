package com.ymmo.ymmoapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class UserController {

    @GetMapping("/user")
    String getAllUsers() {
        return "All users";
    }

    @GetMapping("/user/{id}")
    String getUserById(Long id) {
        return "User with id: " + id;
    }

    @PostMapping("/user")
    String createUser() {
        return "Create a new user";
    }

    @PatchMapping("/user/{id}")
    String updateUser(Long id) {
        return "Update user with id: " + id;
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(Long id) {
        return "Delete user with id: " + id;
    }
}
