package com.example.MiniBankApp.controller;


import com.example.MiniBankApp.model.Transaction;
import com.example.MiniBankApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> initiateMoneyTransfer(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID fromAccountId,
            @RequestParam UUID toAccountId,
            @RequestParam BigDecimal amount) {

        try {
            // Para transferini başlat
            Transaction transaction = transactionService.initiateMoneyTransfer(token, fromAccountId, toAccountId, amount);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            // Hata durumunda hata mesajı döndür
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> viewTransactionHistory(@PathVariable UUID accountId, @RequestHeader("Authorization") String token) {
        try {
            // İşlem geçmişini al
            List<Transaction> transactions = transactionService.viewTransactionHistory(accountId, token);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            // Hata durumunda hata mesajı döndür
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}