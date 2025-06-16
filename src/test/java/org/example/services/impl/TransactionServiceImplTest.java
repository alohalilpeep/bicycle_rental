package org.example.services.impl;

import org.example.models.Transaction;
import org.example.models.enums.TransactionStatus;
import org.example.repositories.TransactionRepository;
import org.example.services.dto.TransactionDto;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionDto validTransactionDto;
    private TransactionDto invalidTransactionDto;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        validTransactionDto = new TransactionDto();
        validTransactionDto.setUserId("user123");
        validTransactionDto.setAmount(BigDecimal.valueOf(100.0));
        validTransactionDto.setPaymentMethodId("pm_123");
        validTransactionDto.setStatus("SUCCESS");
        validTransactionDto.setRideId("ride_456");

        invalidTransactionDto = new TransactionDto(); // Will violate all @NotNull constraints

        transaction = new Transaction();
        transaction.setId("txn123");
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionDate(LocalDateTime.now());
    }

    @Test
    void recordTransactionWithValidDtoShouldSaveTransaction() {
        when(validationUtil.isValid(validTransactionDto)).thenReturn(true);
        when(modelMapper.map(validTransactionDto, Transaction.class)).thenReturn(transaction);

        transactionService.recordTransaction(validTransactionDto);

        verify(transactionRepository, times(1)).save(transaction);
        assertNotNull(transaction.getTransactionDate());
    }

    @Test
    void recordTransactionWithNullUserIdShouldThrowException() {
        validTransactionDto.setUserId(null);
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransactionWithNegativeAmountShouldThrowException() {
        validTransactionDto.setAmount(BigDecimal.valueOf(-10.0));
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransactionWithNullPaymentMethodShouldThrowException() {
        validTransactionDto.setPaymentMethodId(null);
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransactionWithNullStatusShouldThrowException() {
        validTransactionDto.setStatus(null);
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransactionWhenRepositoryFailsShouldPropagateException() {
        when(validationUtil.isValid(validTransactionDto)).thenReturn(true);
        when(modelMapper.map(validTransactionDto, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(any())).thenThrow(new DataAccessException("DB error") {});

        assertThrows(DataAccessException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
    }

    @Test
    void getUserTransactionsShouldReturnUserTransactions() {
        String userId = "user123";
        List<Transaction> expectedTransactions = Collections.singletonList(transaction);
        when(transactionRepository.findByUserId(userId)).thenReturn(expectedTransactions);

        List<Transaction> result = transactionService.getUserTransactions(userId);

        assertEquals(expectedTransactions, result);
        verify(transactionRepository, times(1)).findByUserId(userId);
    }

    @Test
    void updateTransactionStatusWithExistingTransactionShouldUpdateStatus() {
        String transactionId = "txn123";
        TransactionStatus newStatus = TransactionStatus.FAILED;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.updateTransactionStatus(transactionId, newStatus);

        assertEquals(newStatus, transaction.getStatus());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void updateTransactionStatusWithStringStatusShouldHandleConversion() {
        // Arrange
        TransactionDto dtoWithStringStatus = new TransactionDto();
        dtoWithStringStatus.setStatus("PENDING");
        // This test would require additional setup if you need to test string-to-enum conversion
        // Might be better handled in a separate mapper test
    }
}
