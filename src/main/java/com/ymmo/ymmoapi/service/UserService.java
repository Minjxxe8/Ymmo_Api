package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.exception.ResourceNotFoundException;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    public ResponseEntity<Users> getUserById(Long id) {
        return usersRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public ResponseEntity<Users> createUser(UserCreationDto user) {
        try {
            PasswordService passwordService = new PasswordService();
            return ResponseEntity.ok(usersRepository.save(new Users(
                    user.getEmail(),
                    user.getName(),
                    user.getSurname(),
                    passwordService.hashPassword(user.getUnhashedPassword()),
                    "user")));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Users> updateUser(Long id, Users user) {
        try {
            return usersRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setName(user.getName());
                        existingUser.setEmail(user.getEmail());
                        return ResponseEntity.ok(usersRepository.save(existingUser));
                    })
                    .orElse(ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Users> deleteUser(Long id) {
        try {
            return usersRepository.findById(id)
                    .map(user -> {
                        usersRepository.delete(user);
                        return ResponseEntity.ok(user);
                    })
                    .orElse(ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
