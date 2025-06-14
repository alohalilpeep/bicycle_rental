package org.example.services.impl;

import org.example.models.*;
import org.example.models.enums.BikeStatus;
import org.example.models.enums.DiscountType;
import org.example.models.enums.PaymentStatus;
import org.example.repositories.*;
import org.example.services.RideService;
import org.example.services.dto.RideRequestDto;
import org.example.services.dto.RideResponseDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final BikeRepository bikeRepository;
    private final TariffZoneRepository tariffZoneRepository;
    private final InsuranceOptionRepository insuranceOptionRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository,
                           UserRepository userRepository,
                           BikeRepository bikeRepository,
                           TariffZoneRepository tariffZoneRepository,
                           InsuranceOptionRepository insuranceOptionRepository,
                           PromoCodeRepository promoCodeRepository,
                           ValidationUtil validationUtil,
                           ModelMapper modelMapper) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.bikeRepository = bikeRepository;
        this.tariffZoneRepository = tariffZoneRepository;
        this.insuranceOptionRepository = insuranceOptionRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public RideResponseDto startRide(RideRequestDto rideRequestDto) {
        if (!validationUtil.isValid(rideRequestDto)) {
            validationUtil.violations(rideRequestDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            throw new IllegalArgumentException("Invalid ride data");
        }

        User user = userRepository.findById(rideRequestDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Bike bike = bikeRepository.findById(rideRequestDto.getBikeId())
                .orElseThrow(() -> new NoSuchElementException("Bike not found"));
        TariffZone startZone = (TariffZone) tariffZoneRepository.findById(rideRequestDto.getStartZoneId())
                .orElseThrow(() -> new NoSuchElementException("Zone not found"));

        if (bike.getCurrentStatus() != BikeStatus.AVAILABLE) {
            throw new IllegalStateException("Bike is not available");
        }

        Ride ride = new Ride();
        ride.setUser(user);
        ride.setBike(bike);
        ride.setStartZone(startZone);
        ride.setStartTime(LocalDateTime.now());
        ride.setPaymentStatus(PaymentStatus.PENDING);

        bike.setCurrentStatus(BikeStatus.IN_USE);
        bikeRepository.save(bike);

        Ride savedRide = rideRepository.save(ride);
        return modelMapper.map(savedRide, RideResponseDto.class);
    }

    @Transactional
    @Override
    public RideResponseDto endRide(String rideId, String endZoneId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NoSuchElementException("Ride not found"));

        TariffZone endZone = (TariffZone) tariffZoneRepository.findById(endZoneId)
                .orElseThrow(() -> new NoSuchElementException("Zone not found"));

        ride.setEndTime(LocalDateTime.now());
        ride.setEndZone(endZone);

        long minutes = Duration.between(ride.getStartTime(), ride.getEndTime()).toMinutes();
        ride.setDurationMinutes((int) minutes);

        calculateRideCost(ride);
        rideRepository.save(ride);

        Bike bike = ride.getBike();
        bike.setCurrentStatus(BikeStatus.AVAILABLE);
        bike.setCurrentZone(endZone);
        bikeRepository.save(bike);

        return modelMapper.map(ride, RideResponseDto.class);
    }

    private void calculateRideCost(Ride ride) {
        // Calculate base cost based on duration and zone pricing
        TariffZone startZone = ride.getStartZone();
        BigDecimal basePrice = startZone.getBasePrice();
        BigDecimal pricePerMinute = startZone.getPricePerMinute();
        int durationMinutes = ride.getDurationMinutes();

        // Base cost = base price + (duration in minutes * price per minute)
        BigDecimal baseCost = basePrice.add(
                pricePerMinute.multiply(BigDecimal.valueOf(durationMinutes))
        );
        ride.setBaseCost(baseCost);

        // Initialize total cost with base cost
        BigDecimal totalCost = baseCost;

        // Add insurance cost if applicable
        if (ride.getInsurance() != null) {
            totalCost = totalCost.add(ride.getInsurance().getPrice());
        }

        // Apply promo code discount if applicable
        if (ride.getPromoCode() != null) {
            PromoCode promoCode = ride.getPromoCode();
            BigDecimal discountValue = promoCode.getDiscountValue();

            if (promoCode.getDiscountType() == DiscountType.PERCENTAGE) {
                // Percentage discount: subtract percentage of total cost
                BigDecimal discountAmount = totalCost.multiply(discountValue.divide(BigDecimal.valueOf(100)));
                totalCost = totalCost.subtract(discountAmount);
            } else {
                // Fixed amount discount: subtract fixed value
                totalCost = totalCost.subtract(discountValue);
                // Ensure total cost doesn't go below zero
                if (totalCost.compareTo(BigDecimal.ZERO) < 0) {
                    totalCost = BigDecimal.ZERO;
                }
            }

            // Update promo code usage
            promoCode.setCurrentUses(promoCode.getCurrentUses() + 1);
            promoCodeRepository.save(promoCode);
        }

        ride.setTotalCost(totalCost);

        // Calculate taxes (assuming we have a tax rate repository)
        // For simplicity, let's assume there's a standard tax rate of 10%
        BigDecimal taxRate = BigDecimal.valueOf(0.10); // 10%
        BigDecimal taxAmount = totalCost.multiply(taxRate);
        ride.setTaxAmount(taxAmount);

        // Calculate final cost (total + tax)
        BigDecimal finalCost = totalCost.add(taxAmount);
        ride.setFinalCost(finalCost);
    }
}