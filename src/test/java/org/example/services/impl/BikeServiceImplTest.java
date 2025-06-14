package org.example.services.impl;

import org.example.models.Bike;
import org.example.models.enums.BikeStatus;
import org.example.repositories.BikeRepository;
import org.example.services.dto.BikeDto;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BikeServiceImplTest {

    @Mock
    private BikeRepository bikeRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BikeServiceImpl bikeService;

    @Test
    void addBikeWithInvalidDtoShouldNotSave() {
        // Arrange
        BikeDto invalidDto = new BikeDto();
        when(validationUtil.isValid(invalidDto)).thenReturn(false);

        // Act
        bikeService.addBike(invalidDto);

        // Assert
        verify(validationUtil).violations(invalidDto);
        verifyNoInteractions(modelMapper, bikeRepository);
    }

    @Test
    void addBikeWithValidDtoShouldMapAndSave() {
        // Arrange
        BikeDto validDto = new BikeDto();
        Bike mappedBike = new Bike();

        when(validationUtil.isValid(validDto)).thenReturn(true);
        when(modelMapper.map(validDto, Bike.class)).thenReturn(mappedBike);

        // Act
        bikeService.addBike(validDto);

        // Assert
        assertEquals(BikeStatus.AVAILABLE, mappedBike.getCurrentStatus());
        verify(bikeRepository).save(mappedBike);
    }

    @Test
    void findAvailableBikesShouldReturnFilteredList() {
        // Arrange
        Bike availableBike = new Bike();
        availableBike.setCurrentStatus(BikeStatus.AVAILABLE);

        when(bikeRepository.findByCurrentStatus(BikeStatus.AVAILABLE))
                .thenReturn(List.of(availableBike));

        // Act
        List<Bike> result = bikeService.findAvailableBikes();

        // Assert
        assertEquals(1, result.size());
        assertEquals(BikeStatus.AVAILABLE, result.get(0).getCurrentStatus());
    }

    @Test
    void findAvailableBikesWhenNoneAvailableShouldReturnEmptyList() {
        // Arrange
        when(bikeRepository.findByCurrentStatus(BikeStatus.AVAILABLE))
                .thenReturn(Collections.emptyList());

        // Act
        List<Bike> result = bikeService.findAvailableBikes();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void changeBikeStatusWhenBikeExistsShouldUpdateStatus() {
        // Arrange
        String bikeId = "bike-123";
        BikeStatus newStatus = BikeStatus.IN_USE;
        Bike existingBike = new Bike();

        when(bikeRepository.findById(bikeId)).thenReturn(Optional.of(existingBike));

        // Act
        bikeService.changeBikeStatus(bikeId, newStatus);

        // Assert
        assertEquals(newStatus, existingBike.getCurrentStatus());
        verify(bikeRepository).save(existingBike);
    }

    @Test
    void changeBikeStatusWhenBikeNotFoundShouldDoNothing() {
        // Arrange
        String bikeId = "non-existent";
        when(bikeRepository.findById(bikeId)).thenReturn(Optional.empty());

        // Act
        bikeService.changeBikeStatus(bikeId, BikeStatus.IN_USE);

        // Assert
        verify(bikeRepository, never()).save(any());
    }
}
