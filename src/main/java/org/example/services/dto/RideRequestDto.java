package org.example.services.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RideRequestDto {
    @NotNull
    @NotEmpty
    private String userId;

    @NotNull
    @NotEmpty
    private String bikeId;

    @NotNull
    @NotEmpty
    private String startZoneId;

    private String insuranceId;
    private String promoCode;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getBikeId() { return bikeId; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; }
    public String getStartZoneId() { return startZoneId; }
    public void setStartZoneId(String startZoneId) { this.startZoneId = startZoneId; }
    public String getInsuranceId() { return insuranceId; }
    public void setInsuranceId(String insuranceId) { this.insuranceId = insuranceId; }
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
}