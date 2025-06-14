package org.example.services;

import org.example.models.TaxRate;
import org.example.services.dto.TaxRateDto;

import java.util.List;

public interface TaxRateService {
    void createTaxRate(TaxRateDto taxRateDto);

    List<TaxRate> getAllActiveTaxRates();

    void toggleTaxRateStatus(String taxId, boolean isActive);
}
