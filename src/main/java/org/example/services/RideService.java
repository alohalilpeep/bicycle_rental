package org.example.services;

import org.example.services.dto.RideRequestDto;
import org.example.services.dto.RideResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface RideService {
    @Transactional
    RideResponseDto startRide(RideRequestDto rideRequestDto);

    @Transactional
    RideResponseDto endRide(String rideId, String endZoneId);
}
