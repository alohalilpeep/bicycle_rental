package org.example.models;

import jakarta.persistence.*;
import org.example.enums.PaymentMethodType;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod extends BaseEntity {
    private User user;
    private PaymentMethodType methodType;
    private String details;
    private Boolean isDefault = false;

    // Геттеры и сеттеры
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "method_type", nullable = false)
    public PaymentMethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(PaymentMethodType methodType) {
        this.methodType = methodType;
    }

    @Column(name = "details", nullable = false)
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Column(name = "is_default", nullable = false)
    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}