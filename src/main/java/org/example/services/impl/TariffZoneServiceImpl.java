package org.example.services.impl;

import org.example.models.TariffZone;
import org.example.repositories.TariffZoneRepository;
import org.example.services.TariffZoneService;
import org.example.services.dto.TariffZoneDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class TariffZoneServiceImpl implements TariffZoneService {
    private final TariffZoneRepository tariffZoneRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public TariffZoneServiceImpl(TariffZoneRepository tariffZoneRepository,
                                 ValidationUtil validationUtil,
                                 ModelMapper modelMapper) {
        this.tariffZoneRepository = tariffZoneRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createTariffZone(TariffZoneDto tariffZoneDto) {
        if (!validationUtil.isValid(tariffZoneDto)) {
            validationUtil.violations(tariffZoneDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            return;
        }

        if (tariffZoneRepository.findByZoneName(tariffZoneDto.getZoneName()) != null) {
            System.out.println("Zone with this name already exists");
            return;
        }

        TariffZone zone = modelMapper.map(tariffZoneDto, TariffZone.class);
        // Ensure all fields are properly set
        zone.setZoneName(tariffZoneDto.getZoneName());
        zone.setBasePrice(tariffZoneDto.getBasePrice());
        zone.setPricePerMinute(tariffZoneDto.getPricePerMinute());
        tariffZoneRepository.save(zone);
    }

    @Override
    public void updateZonePrices(String zoneId, BigDecimal newBasePrice, BigDecimal newPricePerMinute) {
        Objects.requireNonNull(newBasePrice, "Base price cannot be null");
        Objects.requireNonNull(newPricePerMinute, "Price per minute cannot be null");

        tariffZoneRepository.findById(zoneId).ifPresent(zone -> {
            zone.setBasePrice(newBasePrice);
            zone.setPricePerMinute(newPricePerMinute);
            tariffZoneRepository.save(zone);
        });
    }

    @Override
    public List<TariffZone> getAllTariffZones() {
        return tariffZoneRepository.findAll();
    }
}