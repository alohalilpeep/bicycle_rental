package org.example.services.impl;

import org.example.models.PromoCode;
import org.example.repositories.PromoCodeRepository;
import org.example.services.dto.PromoCodeDto;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceImplTest {

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PromoCodeServiceImpl promoCodeService;

    @Test
    void createPromoCode_WithInvalidDto_ShouldNotProceed() {
        // Arrange
        PromoCodeDto invalidDto = new PromoCodeDto();
        when(validationUtil.isValid(invalidDto)).thenReturn(false);

        // Act
        promoCodeService.createPromoCode(invalidDto);

        // Assert
        verify(validationUtil).violations(invalidDto);
        verifyNoInteractions(promoCodeRepository, modelMapper);
    }

    @Test
    void createPromoCode_WithExistingCode_ShouldNotSave() {
        // Arrange
        PromoCodeDto validDto = createValidDto();
        when(validationUtil.isValid(validDto)).thenReturn(true);
        when(promoCodeRepository.findByCode("SUMMER20")).thenReturn(Optional.of(new PromoCode()));

        // Act
        promoCodeService.createPromoCode(validDto);

        // Assert
        verify(promoCodeRepository, never()).save(any());
    }

    @Test
    void createPromoCode_WithValidNewCode_ShouldSave() {
        // Arrange
        PromoCodeDto validDto = createValidDto();
        PromoCode promoCode = new PromoCode();

        when(validationUtil.isValid(validDto)).thenReturn(true);
        when(promoCodeRepository.findByCode("SUMMER20")).thenReturn(Optional.empty());
        when(modelMapper.map(validDto, PromoCode.class)).thenReturn(promoCode);

        // Act
        promoCodeService.createPromoCode(validDto);

        // Assert
        verify(promoCodeRepository).save(promoCode);
    }

    @Test
    void validatePromoCode_WithNonExistingCode_ShouldReturnFalse() {
        // Arrange
        when(promoCodeRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        // Act
        boolean isValid = promoCodeService.validatePromoCode("INVALID");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validatePromoCode_WithInactiveCode_ShouldReturnFalse() {
        // Arrange
        PromoCode inactiveCode = createTestPromoCode();
        inactiveCode.setIsActive(false);
        when(promoCodeRepository.findByCode("INACTIVE")).thenReturn(Optional.of(inactiveCode));

        // Act
        boolean isValid = promoCodeService.validatePromoCode("INACTIVE");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validatePromoCode_WithExpiredCode_ShouldReturnFalse() {
        // Arrange
        PromoCode expiredCode = createTestPromoCode();
        expiredCode.setValidTo(LocalDate.now().minusDays(1));
        when(promoCodeRepository.findByCode("EXPIRED")).thenReturn(Optional.of(expiredCode));

        // Act
        boolean isValid = promoCodeService.validatePromoCode("EXPIRED");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validatePromoCode_WithNotYetValidCode_ShouldReturnFalse() {
        // Arrange
        PromoCode futureCode = createTestPromoCode();
        futureCode.setValidFrom(LocalDate.now().plusDays(1));
        when(promoCodeRepository.findByCode("FUTURE")).thenReturn(Optional.of(futureCode));

        // Act
        boolean isValid = promoCodeService.validatePromoCode("FUTURE");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validatePromoCode_WithExceededUsage_ShouldReturnFalse() {
        // Arrange
        PromoCode overusedCode = createTestPromoCode();
        overusedCode.setMaxUses(10);
        overusedCode.setCurrentUses(10);
        when(promoCodeRepository.findByCode("OVERUSED")).thenReturn(Optional.of(overusedCode));

        // Act
        boolean isValid = promoCodeService.validatePromoCode("OVERUSED");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validatePromoCode_WithValidCode_ShouldReturnTrue() {
        // Arrange
        PromoCode validCode = createTestPromoCode();
        when(promoCodeRepository.findByCode("VALID")).thenReturn(Optional.of(validCode));

        // Act
        boolean isValid = promoCodeService.validatePromoCode("VALID");

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validatePromoCode_WithNullMaxUses_ShouldReturnTrue() {
        // Arrange
        PromoCode unlimitedCode = createTestPromoCode();
        unlimitedCode.setMaxUses(null);
        unlimitedCode.setCurrentUses(100);
        when(promoCodeRepository.findByCode("UNLIMITED")).thenReturn(Optional.of(unlimitedCode));

        // Act
        boolean isValid = promoCodeService.validatePromoCode("UNLIMITED");

        // Assert
        assertTrue(isValid);
    }

    private PromoCodeDto createValidDto() {
        PromoCodeDto dto = new PromoCodeDto();
        dto.setCode("SUMMER20");
        dto.setValidFrom(LocalDate.now().minusDays(1));
        dto.setValidTo(LocalDate.now().plusDays(30));
        dto.setMaxUses(100);
        return dto;
    }

    private PromoCode createTestPromoCode() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("TEST");
        promoCode.setIsActive(true);
        promoCode.setValidFrom(LocalDate.now().minusDays(1));
        promoCode.setValidTo(LocalDate.now().plusDays(1));
        promoCode.setMaxUses(10);
        promoCode.setCurrentUses(5);
        return promoCode;
    }
}