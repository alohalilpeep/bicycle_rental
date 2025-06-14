package org.example.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.example.models.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "tax_rates")
public class TaxRate extends BaseEntity {
    private String taxName;
    private BigDecimal rate;
    private Boolean isActive = true;
    private String description;

    @Column(name = "tax_name", nullable = false)
    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @Column(name = "rate", nullable = false, precision = 5, scale = 2)
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Column(name = "is_active", nullable = false)
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}