package org.example.services.impl;

import org.example.models.Bike;
import org.example.models.enums.BikeStatus;
import org.example.repositories.BikeRepository;
import org.example.services.BikeService;
import org.example.services.dto.BikeDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeServiceImpl implements BikeService {
    private final BikeRepository bikeRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public BikeServiceImpl(BikeRepository bikeRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper) {
        this.bikeRepository = bikeRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addBike(BikeDto bikeDto) {
        if (!validationUtil.isValid(bikeDto)) {
            validationUtil.violations(bikeDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            return;
        }

        Bike bike = modelMapper.map(bikeDto, Bike.class);
        bike.setCurrentStatus(BikeStatus.AVAILABLE);
        bikeRepository.save(bike);
    }

    @Override
    public List<Bike> findAvailableBikes() {
        return bikeRepository.findByCurrentStatus(BikeStatus.AVAILABLE);
    }

    @Override
    public void changeBikeStatus(String bikeId, BikeStatus status) {
        bikeRepository.findById(bikeId).ifPresent(bike -> {
            bike.setCurrentStatus(status);
            bikeRepository.save(bike);
        });
    }
}