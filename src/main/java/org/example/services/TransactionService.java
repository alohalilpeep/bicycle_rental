package org.example.services;

import org.example.models.Transaction;
import org.example.models.enums.TransactionStatus;
import org.example.services.dto.TransactionDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionService {
    @Transactional
    void recordTransaction(TransactionDto transactionDto);

    List<Transaction> getUserTransactions(String userId);

    void updateTransactionStatus(String transactionId, TransactionStatus status);
}
