package org.example.repositories;

import org.example.models.Transaction;
import org.example.models.User;
import org.example.models.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByUserId(String userId);
}