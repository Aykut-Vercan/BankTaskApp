package com.example.MiniBankApp.service;


import com.example.MiniBankApp.model.Account;
import com.example.MiniBankApp.model.User;
import com.example.MiniBankApp.repository.AccountRepository;
import com.example.MiniBankApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    private String generateAccountNumber() {
        // Hesap numarası oluşturmak için basit bir yöntem
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }

    public Account createAccount(String token, String name, BigDecimal initialBalance) {
        String username = jwtService.getUsernameFromToken(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setName(name);
        account.setNumber(generateAccountNumber());
        account.setBalance(initialBalance);

        return accountRepository.save(account);
    }

    public List<Account> searchAccounts(String token, String number, String name) {
        String username = jwtService.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (number != null && name != null) {
            return accountRepository.findByNumberAndNameAndUserId(number, name, user.getId());
        } else if (number != null) {
            return accountRepository.findByNumber(number);
        } else if (name != null) {
            return accountRepository.findByNameAndUserId(name, user.getId());
        } else {
            return accountRepository.findByUserId(user.getId());
        }
    }

    public void updateAccount(UUID accountId, String token, String name, BigDecimal balance) {
        String username = jwtService.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to update this account");
        }

        account.setName(name);
        account.setBalance(balance);
        accountRepository.save(account);
    }

    public void deleteAccount(UUID accountId, String token) {
        String username = jwtService.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to delete this account");
        }

        accountRepository.delete(account);
    }

    public Account getAccountDetails(UUID accountId, String token) {
        String username = jwtService.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to view this account");
        }

        return account;
    }


}
