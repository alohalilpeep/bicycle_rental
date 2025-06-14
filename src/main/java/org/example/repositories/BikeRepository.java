package org.example.repositories;

import org.example.models.Bike;
import org.example.models.enums.BikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<Bike, String> {
    List<Bike> findByCurrentStatus(BikeStatus status);
    List<Bike> findByModel(String model);
}