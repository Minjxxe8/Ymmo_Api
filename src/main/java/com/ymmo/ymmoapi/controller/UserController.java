package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UsersRepository usersRepository;

    public UserController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return usersRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try {
            return ResponseEntity.ok(usersRepository.save(user));
        } catch (Exception e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users user) {
        try {
            return usersRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setName(user.getName());
                        existingUser.setEmail(user.getEmail());
                        return ResponseEntity.ok(usersRepository.save(existingUser));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Users> deleteUser(@PathVariable Long id) {
        try {
            return usersRepository.findById(id)
                    .map(user -> {
                        usersRepository.delete(user);
                        return ResponseEntity.ok(user);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
