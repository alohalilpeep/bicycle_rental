package org.example.services.impl;

import org.example.models.TaxRate;
import org.example.repositories.TaxRateRepository;
import org.example.services.dto.TaxRateDto;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxRateServiceImplTest {

    @Mock
    private TaxRateRepository taxRateRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaxRateServiceImpl taxRateService;

    private TaxRateDto validTaxRateDto;
    private TaxRateDto invalidTaxRateDto;
    private TaxRate taxRate;

    @BeforeEach
    void setUp() {
        validTaxRateDto = new TaxRateDto();
        validTaxRateDto.setTaxName("VAT");
        validTaxRateDto.setRate(BigDecimal.valueOf(20.0));

        invalidTaxRateDto = new TaxRateDto();
        invalidTaxRateDto.setTaxName("");
        invalidTaxRateDto.setRate(BigDecimal.valueOf(-5.0));

        taxRate = new TaxRate();
        taxRate.setId("1");
        taxRate.setTaxName("VAT");
        taxRate.setRate(BigDecimal.valueOf(20.0));
        taxRate.setIsActive(true);
    }

    @Test
    void createTaxRate_WithValidDto_ShouldSaveTaxRate() {
        // Arrange
        when(validationUtil.isValid(validTaxRateDto)).thenReturn(true);
        when(taxRateRepository.findByTaxName(validTaxRateDto.getTaxName())).thenReturn(null);
        when(modelMapper.map(validTaxRateDto, TaxRate.class)).thenReturn(taxRate);

        // Act
        taxRateService.createTaxRate(validTaxRateDto);

        // Assert
        verify(taxRateRepository, times(1)).save(taxRate);
    }

    @Test
    void createTaxRate_WithInvalidDto_ShouldNotSaveTaxRate() {
        // Arrange
        when(validationUtil.isValid(invalidTaxRateDto)).thenReturn(false);

        // Act
        taxRateService.createTaxRate(invalidTaxRateDto);

        // Assert
        verify(taxRateRepository, never()).save(any());
    }

    @Test
    void createTaxRate_WithExistingTaxName_ShouldNotSaveTaxRate() {
        // Arrange
        when(validationUtil.isValid(validTaxRateDto)).thenReturn(true);
        when(taxRateRepository.findByTaxName(validTaxRateDto.getTaxName())).thenReturn(taxRate);

        // Act
        taxRateService.createTaxRate(validTaxRateDto);

        // Assert
        verify(taxRateRepository, never()).save(any());
    }

    @Test
    void getAllActiveTaxRates_ShouldReturnActiveTaxRates() {
        // Arrange
        List<TaxRate> expectedTaxRates = Collections.singletonList(taxRate);
        when(taxRateRepository.findByIsActiveTrue()).thenReturn(expectedTaxRates);

        // Act
        List<TaxRate> result = taxRateService.getAllActiveTaxRates();

        // Assert
        assertEquals(expectedTaxRates, result);
        verify(taxRateRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void toggleTaxRateStatus_WithExistingId_ShouldUpdateStatus() {
        // Arrange
        String taxId = "1";
        boolean newStatus = false;
        when(taxRateRepository.findById(taxId)).thenReturn(Optional.of(taxRate));

        // Act
        taxRateService.toggleTaxRateStatus(taxId, newStatus);

        // Assert
        assertEquals(newStatus, taxRate.getIsActive());
        verify(taxRateRepository, times(1)).save(taxRate);
    }

    @Test
    void toggleTaxRateStatus_WithNonExistingId_ShouldDoNothing() {
        // Arrange
        String taxId = "999";
        when(taxRateRepository.findById(taxId)).thenReturn(Optional.empty());

        // Act
        taxRateService.toggleTaxRateStatus(taxId, false);

        // Assert
        verify(taxRateRepository, never()).save(any());
    }
}