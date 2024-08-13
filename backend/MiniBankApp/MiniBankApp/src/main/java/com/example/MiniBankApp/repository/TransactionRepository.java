package com.example.MiniBankApp.repository;

import com.example.MiniBankApp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFrom_IdOrTo_Id(UUID fromAccountId, UUID toAccountId);
}