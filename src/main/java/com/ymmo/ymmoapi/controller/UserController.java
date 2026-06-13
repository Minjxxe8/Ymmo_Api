package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.MyUserModificationDto;
import com.ymmo.ymmoapi.dto.UserCreationDto;
import com.ymmo.ymmoapi.dto.WalletsDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.service.PasswordService;
import com.ymmo.ymmoapi.service.UserService;
import com.ymmo.ymmoapi.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final WalletService walletService;

    public UserController(UsersRepository usersRepository, PasswordService passwordService, WalletService walletService) {
        this.userService = new UserService(usersRepository, passwordService, walletService);
        this.walletService = walletService;
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
    public ResponseEntity<?> createUser(@RequestBody UserCreationDto user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }


    @PatchMapping("/users/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Users user) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> myUserInfo() {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(userService.myUserInfo(email));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PatchMapping("/users/me")
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

    @GetMapping("/users/me/wallet")
    public ResponseEntity<?> getMyWallet() {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(walletService.getUserWallet(email));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PatchMapping("/users/me/wallet/deposit")
    public ResponseEntity<?> addBalanceToMyWallet(@RequestBody WalletsDto.WalletModificationBalance walletModificationBalance) {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(walletService.addBalance(email, walletModificationBalance.balance()));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PatchMapping("/users/me/wallet/withdraw")
    public ResponseEntity<?> removeBalanceToMyWallet(@RequestBody WalletsDto.WalletModificationBalance walletModificationBalance) {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(walletService.removeBalance(email, walletModificationBalance.balance()));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }
}
