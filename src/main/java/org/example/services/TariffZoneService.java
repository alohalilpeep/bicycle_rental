package org.example.services;

import org.example.models.TariffZone;
import org.example.services.dto.TariffZoneDto;

import java.math.BigDecimal;
import java.util.List;

public interface TariffZoneService {
    void createTariffZone(TariffZoneDto tariffZoneDto);

    List<TariffZone> getAllTariffZones();

    void updateZonePrices(String zoneId, BigDecimal newBasePrice, BigDecimal newPricePerMinute);
}
