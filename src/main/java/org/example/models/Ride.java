package org.example.models;

import jakarta.persistence.*;
import org.example.models.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
public class Ride extends BaseEntity {
    private User user;
    private Bike bike;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TariffZone startZone;
    private TariffZone endZone;
    private BigDecimal baseCost;
    private Integer durationMinutes;
    private InsuranceOption insurance;
    private PromoCode promoCode;
    private BigDecimal totalCost;
    private BigDecimal taxAmount;
    private BigDecimal finalCost;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "bike_id", nullable = false)
    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    @Column(name = "start_time", nullable = false)
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @ManyToOne
    @JoinColumn(name = "start_zone_id", nullable = false)
    public TariffZone getStartZone() {
        return startZone;
    }

    public void setStartZone(TariffZone startZone) {
        this.startZone = startZone;
    }

    @ManyToOne
    @JoinColumn(name = "end_zone_id")
    public TariffZone getEndZone() {
        return endZone;
    }

    public void setEndZone(TariffZone endZone) {
        this.endZone = endZone;
    }

    @Column(name = "base_cost", precision = 10, scale = 2)
    public BigDecimal getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
    }

    @Column(name = "duration_minutes")
    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    public InsuranceOption getInsurance() {
        return insurance;
    }

    public void setInsurance(InsuranceOption insurance) {
        this.insurance = insurance;
    }

    @ManyToOne
    @JoinColumn(name = "promo_id")
    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }

    @Column(name = "total_cost", precision = 10, scale = 2)
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Column(name = "tax_amount", precision = 10, scale = 2)
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Column(name = "final_cost", precision = 10, scale = 2)
    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}