package org.example.services.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {
    private String id;

    private String rideId;

    @NotNull
    @NotEmpty
    private String userId;

    @NotNull
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private LocalDateTime transactionDate;

    @NotNull
    @NotEmpty
    private String paymentMethodId;

    @NotNull
    private String status; // "SUCCESS", "FAILED", etc.

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRideId() { return rideId; }
    public void setRideId(String rideId) { this.rideId = rideId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}