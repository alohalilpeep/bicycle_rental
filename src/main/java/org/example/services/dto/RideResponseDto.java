package org.example.services.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RideResponseDto {
    private String id;
    private String userId;
    private String bikeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String startZoneId;
    private String endZoneId;
    private BigDecimal baseCost;
    private Integer durationMinutes;
    private String insuranceName;
    private BigDecimal insuranceCost;
    private String promoCode;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal finalCost;
    private String paymentStatus;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}