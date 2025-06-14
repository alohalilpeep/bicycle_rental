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
    void recordTransaction_WithValidDto_ShouldSaveTransaction() {
        // Arrange
        when(validationUtil.isValid(validTransactionDto)).thenReturn(true);
        when(modelMapper.map(validTransactionDto, Transaction.class)).thenReturn(transaction);

        // Act
        transactionService.recordTransaction(validTransactionDto);

        // Assert
        verify(transactionRepository, times(1)).save(transaction);
        assertNotNull(transaction.getTransactionDate());
    }

    @Test
    void recordTransaction_WithNullUserId_ShouldThrowException() {
        // Arrange
        validTransactionDto.setUserId(null);
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransaction_WithNegativeAmount_ShouldThrowException() {
        // Arrange
        validTransactionDto.setAmount(BigDecimal.valueOf(-10.0));
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransaction_WithNullPaymentMethod_ShouldThrowException() {
        // Arrange
        validTransactionDto.setPaymentMethodId(null);
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransaction_WithNullStatus_ShouldThrowException() {
        // Arrange
        validTransactionDto.setStatus(null);
        when(validationUtil.isValid(validTransactionDto)).thenReturn(false);
        when(validationUtil.violations(validTransactionDto))
                .thenReturn(Collections.emptySet());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordTransaction_WhenRepositoryFails_ShouldPropagateException() {
        // Arrange
        when(validationUtil.isValid(validTransactionDto)).thenReturn(true);
        when(modelMapper.map(validTransactionDto, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(any())).thenThrow(new DataAccessException("DB error") {});

        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            transactionService.recordTransaction(validTransactionDto);
        });
    }

    @Test
    void getUserTransactions_ShouldReturnUserTransactions() {
        // Arrange
        String userId = "user123";
        List<Transaction> expectedTransactions = Collections.singletonList(transaction);
        when(transactionRepository.findByUserId(userId)).thenReturn(expectedTransactions);

        // Act
        List<Transaction> result = transactionService.getUserTransactions(userId);

        // Assert
        assertEquals(expectedTransactions, result);
        verify(transactionRepository, times(1)).findByUserId(userId);
    }

    @Test
    void updateTransactionStatus_WithExistingTransaction_ShouldUpdateStatus() {
        // Arrange
        String transactionId = "txn123";
        TransactionStatus newStatus = TransactionStatus.FAILED;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Act
        transactionService.updateTransactionStatus(transactionId, newStatus);

        // Assert
        assertEquals(newStatus, transaction.getStatus());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void updateTransactionStatus_WithStringStatus_ShouldHandleConversion() {
        // Arrange
        TransactionDto dtoWithStringStatus = new TransactionDto();
        dtoWithStringStatus.setStatus("PENDING");
        // This test would require additional setup if you need to test string-to-enum conversion
        // Might be better handled in a separate mapper test
    }
}