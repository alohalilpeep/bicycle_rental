package org.example.services.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class TariffZoneDto {
    private String id;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 50, message = "Zone name must be between 2 and 50 characters")
    private String zoneName;

    @NotNull
    @PositiveOrZero(message = "Base price must be positive or zero")
    private BigDecimal basePrice;

    @NotNull
    @PositiveOrZero(message = "Price per minute must be positive or zero")
    private BigDecimal pricePerMinute;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public BigDecimal getPricePerMinute() { return pricePerMinute; }
    public void setPricePerMinute(BigDecimal pricePerMinute) { this.pricePerMinute = pricePerMinute; }
}