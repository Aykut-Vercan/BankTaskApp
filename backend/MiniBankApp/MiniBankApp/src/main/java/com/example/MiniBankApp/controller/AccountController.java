package com.example.MiniBankApp.controller;

import com.example.MiniBankApp.model.Account;
import com.example.MiniBankApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")

public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<?> handleAccountRequest(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> requestBody) {

        // "Bearer " kısmını JWT token'dan çıkar
        String jwtToken = token.replace("Bearer ", "");

        // İşlem türünü belirle
        String action = (String) requestBody.get("action");

        if ("create".equalsIgnoreCase(action)) {
            String name = (String) requestBody.get("name");
            Object initialBalanceObj = requestBody.get("initialBalance");

            BigDecimal initialBalance;
            if (initialBalanceObj instanceof Number) {
                initialBalance = new BigDecimal(((Number) initialBalanceObj).doubleValue());
            } else {
                return ResponseEntity.badRequest().body("Invalid initialBalance type");
            }

            Account account = accountService.createAccount(jwtToken, name, initialBalance);
            return ResponseEntity.ok(account);

        } else if ("search".equalsIgnoreCase(action)) {
            String number = (String) requestBody.get("number");
            String name = (String) requestBody.get("name");
            List<Account> accounts = accountService.searchAccounts(jwtToken, number, name);
            return ResponseEntity.ok(accounts);
        } else {
            return ResponseEntity.badRequest().body("Invalid action");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String token,
            @RequestParam String name,
            @RequestParam BigDecimal balance) {

        String jwtToken = token.replace("Bearer ", "");
        accountService.updateAccount(id, jwtToken, name, balance);
        return ResponseEntity.ok("Account updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountDetails(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.replace("Bearer ", "");
        Account account = accountService.getAccountDetails(id, jwtToken);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.replace("Bearer ", "");
        accountService.deleteAccount(id, jwtToken);
        return ResponseEntity.ok("Account deleted successfully");
    }
}