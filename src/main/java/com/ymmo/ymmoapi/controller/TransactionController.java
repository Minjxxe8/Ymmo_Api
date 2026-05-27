package com.ymmo.ymmoapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

// TODO: Change the return data to DTO
public class TransactionController {

    @GetMapping("/transactions")
    String getAllTransactions() {
        return "All transactions";
    }

    @GetMapping("/transactions/{id}")
    String getTransactionById(Long id) {
        return "Transaction with id: " + id;
    }

    @PostMapping("/transactions")
    String createTransaction() {
        return "Create a new transaction";
    }

    @PatchMapping("/transactions/{id}")
    String updateTransaction(Long id) {
        return "Update transaction with id: " + id;
    }

    @DeleteMapping("/transactions/{id}")
    String deleteTransaction(Long id) {
        return "Delete transaction with id: " + id;
    }
}
