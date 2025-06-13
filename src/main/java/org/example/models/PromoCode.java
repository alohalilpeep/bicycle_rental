package org.example.models;

import jakarta.persistence.*;
import org.example.enums.DiscountType;
import org.example.models.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promo_codes")
public class PromoCode extends BaseEntity {
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer maxUses;
    private Integer currentUses = 0;
    private Boolean isActive = true;

    // Геттеры и сеттеры
    @Column(name = "code", nullable = false, unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    @Column(name = "valid_from", nullable = false)
    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    @Column(name = "valid_to", nullable = false)
    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    @Column(name = "max_uses")
    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    @Column(name = "current_uses", nullable = false)
    public Integer getCurrentUses() {
        return currentUses;
    }

    public void setCurrentUses(Integer currentUses) {
        this.currentUses = currentUses;
    }

    @Column(name = "is_active", nullable = false)
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}