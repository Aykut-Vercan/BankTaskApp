package com.example.MiniBankApp.repository;

import com.example.MiniBankApp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findByUserId(UUID userId);

    List<Account> findByNumber(String number);

    List<Account> findByNameAndUserId(String name, UUID userId);

    List<Account> findByNumberAndNameAndUserId(String name, String number, UUID userId);
}