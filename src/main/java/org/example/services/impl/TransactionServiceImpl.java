package org.example.services.impl;

import org.example.models.Transaction;
import org.example.models.enums.TransactionStatus;
import org.example.repositories.TransactionRepository;
import org.example.services.TransactionService;
import org.example.services.dto.TransactionDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  ValidationUtil validationUtil,
                                  ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void recordTransaction(TransactionDto transactionDto) {
        if (!validationUtil.isValid(transactionDto)) {
            validationUtil.violations(transactionDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            throw new IllegalArgumentException("Invalid transaction data");
        }

        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getUserTransactions(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public void updateTransactionStatus(String transactionId, TransactionStatus status) {
        transactionRepository.findById(transactionId).ifPresent(transaction -> {
            transaction.setStatus(status);
            transactionRepository.save(transaction);
        });
    }
}