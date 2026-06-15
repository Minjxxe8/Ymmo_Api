package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.TransactionCreationDto;
import com.ymmo.ymmoapi.exception.ResourceNotFoundException;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

// TODO: Change the return data to DTO
@RestController
@RequestMapping("/api")
@CrossOrigin
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions() {
        try {
            return ResponseEntity.ok(transactionService.getAllTransactions());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionById(id));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @GetMapping("/transactions/user/{userId}")
    public ResponseEntity<?> getTransactionsByUserId(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionCreationDto transactionCreationDto) {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(transactionService.createTransaction(email, transactionCreationDto));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }
}
