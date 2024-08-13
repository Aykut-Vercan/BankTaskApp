package com.example.MiniBankApp.service;

import com.example.MiniBankApp.model.Account;
import com.example.MiniBankApp.model.Transaction;
import com.example.MiniBankApp.model.User;
import com.example.MiniBankApp.repository.AccountRepository;
import com.example.MiniBankApp.repository.TransactionRepository;
import com.example.MiniBankApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public Transaction initiateMoneyTransfer(String token, UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        String username = jwtService.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Hesapları bul
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        // Kullanıcının bu hesap üzerinde yetkili olup olmadığını kontrol et
        if (!fromAccount.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to transfer from this account");
        }

        // Transfer miktarını kontrol et
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // İşlemi yap ve Transaction nesnesini oluştur
        Transaction transaction = new Transaction();
        transaction.setFrom(fromAccount);
        transaction.setTo(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        try {
            // Kaynaktaki hesapta yeterli bakiye olup olmadığını kontrol et
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                throw new IllegalArgumentException("Insufficient balance");
            }

            // Transfer işlemi
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            transactionRepository.save(transaction);

        } catch (Exception e) {
            // Bir hata oluşursa işlem başarısız olarak işaretle
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw e; // Hatanın tekrar fırlatılması
        }

        return transaction;
    }

    public List<Transaction> viewTransactionHistory(UUID accountId, String token) {
        String username = jwtService.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized to view this account");
        }

        return transactionRepository.findByFrom_IdOrTo_Id(accountId, accountId);
    }
}