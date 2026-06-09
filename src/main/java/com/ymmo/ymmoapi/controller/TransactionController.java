package com.ymmo.ymmoapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// TODO: Change the return data to DTO
@RestController
@RequestMapping("/api")
@CrossOrigin
public class TransactionController {

    @GetMapping("/transactions")
    String getAllTransactions() {
        return "All transactions";
    }

    @GetMapping("/transactions/{id}")
    String getTransactionById(@PathVariable Long id) {
        return "Transaction with id: " + id;
    }

    @PostMapping("/transactions")
    String createTransaction() {
        return "Create a new transaction";
    }

    @PatchMapping("/transactions/{id}")
    String updateTransaction(@PathVariable Long id) {
        return "Update transaction with id: " + id;
    }

    @DeleteMapping("/transactions/{id}")
    @PreAuthorize("hasRole('admin' || 'agent')")
    String deleteTransaction(@PathVariable Long id) {
        return "Delete transaction with id: " + id;
    }
}
