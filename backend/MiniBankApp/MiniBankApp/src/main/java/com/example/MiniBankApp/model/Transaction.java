package com.example.MiniBankApp.model;

import lombok.Data;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account from;

    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account to;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_date", updatable = false, nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }

    public enum TransactionStatus {
        SUCCESS,
        FAILED
    }
}