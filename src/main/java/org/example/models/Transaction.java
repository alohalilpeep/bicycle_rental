package org.example.models;

import jakarta.persistence.*;
import org.example.enums.TransactionStatus;
import org.example.models.BaseEntity;
import org.example.models.PaymentMethod;
import org.example.models.Ride;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {
    private Ride ride;
    private User user;
    private BigDecimal amount;
    private LocalDateTime transactionDate = LocalDateTime.now();
    private PaymentMethod paymentMethod;
    private TransactionStatus status;

    // Геттеры и сеттеры
    @ManyToOne
    @JoinColumn(name = "ride_id")
    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "transaction_date", nullable = false)
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}