package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dao.UsersDao;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UsersDao usersDao;

    @GetMapping("/users")
    String getAllUsers() {
        return "All users";
    }

    @GetMapping("/users/{id}")
    String getUserById(@PathVariable Long id) {
        return "User with id: " + id;
    }

    @PostMapping("/users")
    String createUser() {
        return "Create a new user";
    }

    @PatchMapping("/users/{id}")
    String updateUser(@PathVariable Long id) {
        return "Update user with id: " + id;
    }

    @DeleteMapping("/users/{id}")
    String deleteUser(@PathVariable Long id) {
        return "Delete user with id: " + id;
    }
}
