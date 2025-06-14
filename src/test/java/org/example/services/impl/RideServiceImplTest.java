package org.example.services.impl;

import org.example.models.*;
import org.example.models.enums.BikeStatus;
import org.example.models.enums.DiscountType;
import org.example.models.enums.PaymentStatus;
import org.example.repositories.*;
import org.example.services.dto.RideRequestDto;
import org.example.services.dto.RideResponseDto;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BikeRepository bikeRepository;

    @Mock
    private TariffZoneRepository tariffZoneRepository;

    @Mock
    private InsuranceOptionRepository insuranceOptionRepository;

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RideServiceImpl rideService;

    private RideRequestDto rideRequestDto;
    private User user;
    private Bike bike;
    private TariffZone startZone;
    private Ride ride;
    private RideResponseDto rideResponseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user1");

        bike = new Bike();
        bike.setId("bike1");
        bike.setCurrentStatus(BikeStatus.AVAILABLE);

        startZone = new TariffZone();
        startZone.setId("zone1");
        startZone.setBasePrice(BigDecimal.valueOf(1.0));
        startZone.setPricePerMinute(BigDecimal.valueOf(0.5));

        rideRequestDto = new RideRequestDto();
        rideRequestDto.setUserId("user1");
        rideRequestDto.setBikeId("bike1");
        rideRequestDto.setStartZoneId("zone1");

        ride = new Ride();
        ride.setId("ride1");
        ride.setUser(user);
        ride.setBike(bike);
        ride.setStartZone(startZone);
        ride.setStartTime(LocalDateTime.now());
        ride.setPaymentStatus(PaymentStatus.PENDING);

        rideResponseDto = new RideResponseDto();
        rideResponseDto.setId("ride1");
    }

    @Test
    void startRideValidRequestReturnsRideResponse() {
        when(validationUtil.isValid(rideRequestDto)).thenReturn(true);
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(bikeRepository.findById("bike1")).thenReturn(Optional.of(bike));
        when(tariffZoneRepository.findById("zone1")).thenReturn(Optional.of(startZone));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
        when(modelMapper.map(ride, RideResponseDto.class)).thenReturn(rideResponseDto);

        RideResponseDto result = rideService.startRide(rideRequestDto);

        assertNotNull(result);
        assertEquals("ride1", result.getId());
        verify(bikeRepository).save(bike);
        assertEquals(BikeStatus.IN_USE, bike.getCurrentStatus());
    }

    @Test
    void startRideInvalidRequestThrowsException() {
        when(validationUtil.isValid(rideRequestDto)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> rideService.startRide(rideRequestDto));
    }

    @Test
    void startRideUserNotFoundThrowsException() {
        when(validationUtil.isValid(rideRequestDto)).thenReturn(true);
        when(userRepository.findById("user1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rideService.startRide(rideRequestDto));
    }

    @Test
    void startRideBikeNotFoundThrowsException() {
        when(validationUtil.isValid(rideRequestDto)).thenReturn(true);
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(bikeRepository.findById("bike1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rideService.startRide(rideRequestDto));
    }

    @Test
    void startRideZoneNotFoundThrowsException() {
        when(validationUtil.isValid(rideRequestDto)).thenReturn(true);
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(bikeRepository.findById("bike1")).thenReturn(Optional.of(bike));
        when(tariffZoneRepository.findById("zone1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rideService.startRide(rideRequestDto));
    }

    @Test
    void startRideBikeNotAvailableThrowsException() {
        bike.setCurrentStatus(BikeStatus.MAINTENANCE);

        // Mock all required repository calls that happen before bike status check
        when(validationUtil.isValid(rideRequestDto)).thenReturn(true);
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(bikeRepository.findById("bike1")).thenReturn(Optional.of(bike));
        when(tariffZoneRepository.findById("zone1")).thenReturn(Optional.of(startZone));

        assertThrows(IllegalStateException.class, () -> rideService.startRide(rideRequestDto));
    }

    @Test
    void endRideValidRequestReturnsRideResponse() {
        TariffZone endZone = new TariffZone();
        endZone.setId("zone2");

        when(rideRepository.findById("ride1")).thenReturn(Optional.of(ride));
        when(tariffZoneRepository.findById("zone2")).thenReturn(Optional.of(endZone));
        when(modelMapper.map(ride, RideResponseDto.class)).thenReturn(rideResponseDto);

        RideResponseDto result = rideService.endRide("ride1", "zone2");

        assertNotNull(result);
        assertEquals("ride1", result.getId());
        assertNotNull(ride.getEndTime());
        assertEquals(endZone, ride.getEndZone());
        assertTrue(ride.getDurationMinutes() >= 0);
        verify(bikeRepository).save(bike);
        assertEquals(BikeStatus.AVAILABLE, bike.getCurrentStatus());
        assertEquals(endZone, bike.getCurrentZone());
    }

    @Test
    void endRideRideNotFoundThrowsException() {
        when(rideRepository.findById("ride1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rideService.endRide("ride1", "zone2"));
    }

    @Test
    void endRideEndZoneNotFoundThrowsException() {
        when(rideRepository.findById("ride1")).thenReturn(Optional.of(ride));
        when(tariffZoneRepository.findById("zone2")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> rideService.endRide("ride1", "zone2"));
    }

    @Test
    void endRideCalculatesCostCorrectlyBaseCostOnly() {
        // Setup
        TariffZone endZone = new TariffZone();
        endZone.setId("zone2");

        Ride rideWithDuration = new Ride();
        rideWithDuration.setId("ride1");
        rideWithDuration.setUser(user);
        rideWithDuration.setBike(bike);
        rideWithDuration.setStartZone(startZone);
        rideWithDuration.setStartTime(LocalDateTime.now().minusMinutes(30));
        rideWithDuration.setPaymentStatus(PaymentStatus.PENDING);

        when(rideRepository.findById("ride1")).thenReturn(Optional.of(rideWithDuration));
        when(tariffZoneRepository.findById("zone2")).thenReturn(Optional.of(endZone));
        when(modelMapper.map(rideWithDuration, RideResponseDto.class)).thenReturn(rideResponseDto);

        // Execute
        RideResponseDto result = rideService.endRide("ride1", "zone2");

        // Verify calculations
        BigDecimal expectedBase = startZone.getBasePrice()
                .add(startZone.getPricePerMinute().multiply(BigDecimal.valueOf(30)));
        BigDecimal expectedTax = expectedBase.multiply(BigDecimal.valueOf(0.1));
        BigDecimal expectedFinal = expectedBase.add(expectedTax);

        assertEquals(expectedBase, rideWithDuration.getBaseCost());
        assertEquals(expectedBase, rideWithDuration.getTotalCost());
        assertEquals(expectedTax, rideWithDuration.getTaxAmount());
        assertEquals(expectedFinal, rideWithDuration.getFinalCost());
    }

    @Test
    void endRideCalculatesCostCorrectlyWithInsurance() {
        // Setup
        TariffZone endZone = new TariffZone();
        endZone.setId("zone2");

        InsuranceOption insurance = new InsuranceOption();
        insurance.setPrice(BigDecimal.valueOf(2.5));

        Ride rideWithDuration = new Ride();
        rideWithDuration.setId("ride1");
        rideWithDuration.setUser(user);
        rideWithDuration.setBike(bike);
        rideWithDuration.setStartZone(startZone);
        rideWithDuration.setStartTime(LocalDateTime.now().minusMinutes(30));
        rideWithDuration.setPaymentStatus(PaymentStatus.PENDING);
        rideWithDuration.setInsurance(insurance);

        when(rideRepository.findById("ride1")).thenReturn(Optional.of(rideWithDuration));
        when(tariffZoneRepository.findById("zone2")).thenReturn(Optional.of(endZone));
        when(modelMapper.map(rideWithDuration, RideResponseDto.class)).thenReturn(rideResponseDto);

        // Execute
        RideResponseDto result = rideService.endRide("ride1", "zone2");

        // Verify calculations
        BigDecimal expectedBase = startZone.getBasePrice()
                .add(startZone.getPricePerMinute().multiply(BigDecimal.valueOf(30)));
        BigDecimal expectedTotal = expectedBase.add(insurance.getPrice());
        BigDecimal expectedTax = expectedTotal.multiply(BigDecimal.valueOf(0.1));
        BigDecimal expectedFinal = expectedTotal.add(expectedTax);

        assertEquals(expectedBase, rideWithDuration.getBaseCost());
        assertEquals(expectedTotal, rideWithDuration.getTotalCost());
        assertEquals(expectedTax, rideWithDuration.getTaxAmount());
        assertEquals(expectedFinal, rideWithDuration.getFinalCost());
    }

    @Test
    void endRideCalculatesCostCorrectlyWithPercentagePromoCode() {
        // Setup
        TariffZone endZone = new TariffZone();
        endZone.setId("zone2");

        PromoCode promoCode = new PromoCode();
        promoCode.setDiscountType(DiscountType.PERCENTAGE);
        promoCode.setDiscountValue(BigDecimal.valueOf(20)); // 20% discount

        Ride rideWithDuration = new Ride();
        rideWithDuration.setId("ride1");
        rideWithDuration.setUser(user);
        rideWithDuration.setBike(bike);
        rideWithDuration.setStartZone(startZone);
        rideWithDuration.setStartTime(LocalDateTime.now().minusMinutes(30));
        rideWithDuration.setPaymentStatus(PaymentStatus.PENDING);
        rideWithDuration.setPromoCode(promoCode);

        when(rideRepository.findById("ride1")).thenReturn(Optional.of(rideWithDuration));
        when(tariffZoneRepository.findById("zone2")).thenReturn(Optional.of(endZone));
        when(modelMapper.map(rideWithDuration, RideResponseDto.class)).thenReturn(rideResponseDto);
        when(promoCodeRepository.save(promoCode)).thenReturn(promoCode);

        // Execute
        RideResponseDto result = rideService.endRide("ride1", "zone2");

        // Verify calculations
        BigDecimal expectedBase = startZone.getBasePrice()
                .add(startZone.getPricePerMinute().multiply(BigDecimal.valueOf(30)));
        BigDecimal expectedTotal = expectedBase.multiply(BigDecimal.valueOf(0.8)); // 20% off
        BigDecimal expectedTax = expectedTotal.multiply(BigDecimal.valueOf(0.1));
        BigDecimal expectedFinal = expectedTotal.add(expectedTax);

        assertEquals(expectedBase, rideWithDuration.getBaseCost());
        assertEquals(expectedTotal, rideWithDuration.getTotalCost());
        assertEquals(expectedTax, rideWithDuration.getTaxAmount());
        assertEquals(expectedFinal, rideWithDuration.getFinalCost());
        verify(promoCodeRepository).save(promoCode);
        assertEquals(1, promoCode.getCurrentUses());
    }
}
