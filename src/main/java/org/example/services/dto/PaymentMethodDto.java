package org.example.services.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PaymentMethodDto {
    private String id;

    @NotNull
    @NotEmpty
    private String userId;

    @NotNull
    private String methodType; // "ONLINE_BANK", "CREDIT_CARD", etc.

    @NotNull
    @NotEmpty
    private String details; // Зашифрованные данные

    private boolean isDefault;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMethodType() { return methodType; }
    public void setMethodType(String methodType) { this.methodType = methodType; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}