package org.example.services.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class TaxRateDto {
    private String id;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 50, message = "Tax name must be between 2 and 50 characters")
    private String taxName;

    @NotNull
    @PositiveOrZero(message = "Tax rate must be positive or zero")
    @Digits(integer = 3, fraction = 2, message = "Tax rate must have up to 3 integer and 2 fraction digits")
    private BigDecimal rate;

    @NotNull
    private Boolean isActive = true;

    @Length(max = 500, message = "Description too long")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}