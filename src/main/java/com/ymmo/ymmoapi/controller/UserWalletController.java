package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.WalletsDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/users/me/wallet")
@CrossOrigin
public class UserWalletController {
    private final WalletService walletService;

    public UserWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping()
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

    @PatchMapping("/deposit")
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

    @PatchMapping("/withdraw")
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
