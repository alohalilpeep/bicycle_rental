package org.example.services.impl;

import org.example.models.InsuranceOption;
import org.example.repositories.InsuranceOptionRepository;
import org.example.services.dto.InsuranceOptionDto;
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
class InsuranceOptionServiceImplTest {

    @Mock
    private InsuranceOptionRepository insuranceOptionRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InsuranceOptionServiceImpl insuranceOptionService;

    @Test
    void createInsuranceOptionWithInvalidDtoShouldNotSave() {
        // Arrange
        InsuranceOptionDto invalidDto = new InsuranceOptionDto();
        when(validationUtil.isValid(invalidDto)).thenReturn(false);

        // Act
        insuranceOptionService.createInsuranceOption(invalidDto);

        // Assert
        verify(validationUtil).violations(invalidDto);
        verifyNoInteractions(modelMapper, insuranceOptionRepository);
    }

    @Test
    void createInsuranceOptionWithValidDtoShouldMapAndSave() {
        // Arrange
        InsuranceOptionDto validDto = new InsuranceOptionDto();
        InsuranceOption mappedOption = new InsuranceOption();

        when(validationUtil.isValid(validDto)).thenReturn(true);
        when(modelMapper.map(validDto, InsuranceOption.class)).thenReturn(mappedOption);

        // Act
        insuranceOptionService.createInsuranceOption(validDto);

        // Assert
        verify(insuranceOptionRepository).save(mappedOption);
    }

    @Test
    void getAllActiveInsuranceOptionsWhenOptionsExistShouldReturnList() {
        // Arrange
        InsuranceOption activeOption = new InsuranceOption();
        activeOption.setIsActive(true);

        when(insuranceOptionRepository.findByIsActiveTrue())
                .thenReturn(List.of(activeOption));

        // Act
        List<InsuranceOption> result = insuranceOptionService.getAllActiveInsuranceOptions();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsActive());
    }

    @Test
    void getAllActiveInsuranceOptionsWhenNoOptionsShouldReturnEmptyList() {
        // Arrange
        when(insuranceOptionRepository.findByIsActiveTrue())
                .thenReturn(Collections.emptyList());

        // Act
        List<InsuranceOption> result = insuranceOptionService.getAllActiveInsuranceOptions();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toggleInsuranceOptionStatusWhenOptionExistsShouldUpdateStatus() {
        // Arrange
        String optionId = "ins-123";
        InsuranceOption existingOption = new InsuranceOption();
        existingOption.setIsActive(false);

        when(insuranceOptionRepository.findById(optionId))
                .thenReturn(Optional.of(existingOption));

        // Act
        insuranceOptionService.toggleInsuranceOptionStatus(optionId, true);

        // Assert
        assertTrue(existingOption.getIsActive());
        verify(insuranceOptionRepository).save(existingOption);
    }

    @Test
    void toggleInsuranceOptionStatusWhenOptionNotFoundShouldDoNothing() {
        // Arrange
        String optionId = "non-existent";
        when(insuranceOptionRepository.findById(optionId))
                .thenReturn(Optional.empty());

        // Act
        insuranceOptionService.toggleInsuranceOptionStatus(optionId, true);

        // Assert
        verify(insuranceOptionRepository, never()).save(any());
    }

    @Test
    void toggleInsuranceOptionStatusWhenOptionExistsShouldDeactivate() {
        // Arrange
        String optionId = "ins-456";
        InsuranceOption existingOption = new InsuranceOption();
        existingOption.setIsActive(true);

        when(insuranceOptionRepository.findById(optionId))
                .thenReturn(Optional.of(existingOption));

        // Act
        insuranceOptionService.toggleInsuranceOptionStatus(optionId, false);

        // Assert
        assertFalse(existingOption.getIsActive());
        verify(insuranceOptionRepository).save(existingOption);
    }
}
