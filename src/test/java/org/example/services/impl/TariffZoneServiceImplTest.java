package org.example.services.impl;

import org.example.models.TariffZone;
import org.example.repositories.TariffZoneRepository;
import org.example.services.dto.TariffZoneDto;
import org.example.services.impl.TariffZoneServiceImpl;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffZoneServiceImplTest {

    @Mock
    private TariffZoneRepository tariffZoneRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TariffZoneServiceImpl tariffZoneService;

    @Test
    void createTariffZone_WithInvalidDto_ShouldNotProceed() {
        // Arrange
        TariffZoneDto invalidDto = new TariffZoneDto();
        when(validationUtil.isValid(invalidDto)).thenReturn(false);

        // Act
        tariffZoneService.createTariffZone(invalidDto);

        // Assert
        verify(validationUtil).violations(invalidDto);
        verifyNoInteractions(tariffZoneRepository, modelMapper);
    }

    @Test
    void createTariffZone_WithExistingZoneName_ShouldNotSave() {
        // Arrange
        TariffZoneDto validDto = createValidDto();
        when(validationUtil.isValid(validDto)).thenReturn(true);
        when(tariffZoneRepository.findByZoneName("Downtown")).thenReturn(Optional.of(new TariffZone()));

        // Act
        tariffZoneService.createTariffZone(validDto);

        // Assert
        verify(tariffZoneRepository, never()).save(any());
    }

    @Test
    void createTariffZone_WithValidNewZone_ShouldSave() {
        // Arrange
        TariffZoneDto validDto = createValidDto();
        TariffZone zone = new TariffZone();
        zone.setZoneName(validDto.getZoneName());
        zone.setBasePrice(validDto.getBasePrice());
        zone.setPricePerMinute(validDto.getPricePerMinute());

        when(validationUtil.isValid(validDto)).thenReturn(true);
        when(tariffZoneRepository.findByZoneName("Downtown")).thenReturn(null);
        when(modelMapper.map(validDto, TariffZone.class)).thenReturn(zone);

        // Act
        tariffZoneService.createTariffZone(validDto);

        // Assert
        verify(tariffZoneRepository).save(zone);
        assertEquals("Downtown", zone.getZoneName());
    }

    @Test
    void getAllTariffZones_ShouldReturnAllZones() {
        // Arrange
        TariffZone zone1 = createTestZone("Zone1");
        TariffZone zone2 = createTestZone("Zone2");
        when(tariffZoneRepository.findAll()).thenReturn(List.of(zone1, zone2));

        // Act
        List<TariffZone> result = tariffZoneService.getAllTariffZones();

        // Assert
        assertEquals(2, result.size());
        verify(tariffZoneRepository).findAll();
    }

    @Test
    void updateZonePrices_WithExistingZone_ShouldUpdatePrices() {
        // Arrange
        String zoneId = "zone-123";
        BigDecimal newBasePrice = new BigDecimal("5.00");
        BigDecimal newPricePerMinute = new BigDecimal("0.25");
        TariffZone existingZone = createTestZone("Downtown");

        when(tariffZoneRepository.findById(zoneId)).thenReturn(Optional.of(existingZone));

        // Act
        tariffZoneService.updateZonePrices(zoneId, newBasePrice, newPricePerMinute);

        // Assert
        assertEquals(newBasePrice, existingZone.getBasePrice());
        assertEquals(newPricePerMinute, existingZone.getPricePerMinute());
        verify(tariffZoneRepository).save(existingZone);
    }

    @Test
    void updateZonePrices_WithNonExistingZone_ShouldDoNothing() {
        // Arrange
        String zoneId = "non-existent";
        when(tariffZoneRepository.findById(zoneId)).thenReturn(Optional.empty());

        // Act
        tariffZoneService.updateZonePrices(zoneId, new BigDecimal("5.00"), new BigDecimal("0.25"));

        // Assert
        verify(tariffZoneRepository, never()).save(any());
    }

    @Test
    void updateZonePrices_WithNullPrices_ShouldThrowException() {
        // Act & Assert - no repository mocking needed since exception is thrown first
        assertThrows(NullPointerException.class, () ->
                tariffZoneService.updateZonePrices("zone-123", null, new BigDecimal("0.25")));
        assertThrows(NullPointerException.class, () ->
                tariffZoneService.updateZonePrices("zone-123", new BigDecimal("5.00"), null));
        assertThrows(NullPointerException.class, () ->
                tariffZoneService.updateZonePrices("zone-123", null, null));
    }

    private TariffZoneDto createValidDto() {
        TariffZoneDto dto = new TariffZoneDto();
        dto.setZoneName("Downtown");
        dto.setBasePrice(new BigDecimal("3.50"));
        dto.setPricePerMinute(new BigDecimal("0.20"));
        return dto;
    }

    private TariffZone createTestZone(String name) {
        TariffZone zone = new TariffZone();
        zone.setId("test-id");
        zone.setZoneName(name);
        zone.setBasePrice(new BigDecimal("3.00"));
        zone.setPricePerMinute(new BigDecimal("0.15"));
        return zone;
    }
}