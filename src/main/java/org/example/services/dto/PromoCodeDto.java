package org.example.services.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PromoCodeDto {
    private String id;

    @NotNull(message = "Promo code cannot be null")
    @NotEmpty(message = "Promo code cannot be empty")
    @Length(min = 3, max = 20, message = "Promo code must be between 3 and 20 characters")
    private String code;

    @NotNull(message = "Discount type cannot be null")
    private String discountType;

    @NotNull(message = "Discount value cannot be null")
    @PositiveOrZero(message = "Discount value must be positive or zero")
    private BigDecimal discountValue;

    @NotNull(message = "Start date cannot be null")
    private LocalDate validFrom;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDate validTo;

    @PositiveOrZero(message = "Max uses must be positive or zero")
    private Integer maxUses;

    public PromoCodeDto() {
    }

    public PromoCodeDto(String code, String discountType, BigDecimal discountValue,
                        LocalDate validFrom, LocalDate validTo, Integer maxUses) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.maxUses = maxUses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    @Override
    public String toString() {
        return "PromoCodeDto{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", maxUses=" + maxUses +
                '}';
    }
}