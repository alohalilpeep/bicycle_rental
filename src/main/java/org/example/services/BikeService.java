package org.example.services;

import org.example.models.Bike;
import org.example.models.enums.BikeStatus;
import org.example.services.dto.BikeDto;

import java.util.List;

public interface BikeService {
    void addBike(BikeDto bikeDto);

    List<Bike> findAvailableBikes();

    void changeBikeStatus(String bikeId, BikeStatus status);
}
