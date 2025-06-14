package org.example.services.impl;

import org.example.models.TaxRate;
import org.example.repositories.TaxRateRepository;
import org.example.services.TaxRateService;
import org.example.services.dto.TaxRateDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxRateServiceImpl implements TaxRateService {
    private final TaxRateRepository taxRateRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public TaxRateServiceImpl(TaxRateRepository taxRateRepository,
                              ValidationUtil validationUtil,
                              ModelMapper modelMapper) {
        this.taxRateRepository = taxRateRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createTaxRate(TaxRateDto taxRateDto) {
        if (!validationUtil.isValid(taxRateDto)) {
            validationUtil.violations(taxRateDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            return;
        }

        if (taxRateRepository.findByTaxName(taxRateDto.getTaxName()) != null) {
            System.out.println("Tax rate with this name already exists");
            return;
        }

        TaxRate taxRate = modelMapper.map(taxRateDto, TaxRate.class);
        taxRateRepository.save(taxRate);
    }

    @Override
    public List<TaxRate> getAllActiveTaxRates() {
        return taxRateRepository.findByIsActiveTrue();
    }

    @Override
    public void toggleTaxRateStatus(String taxId, boolean isActive) {
        taxRateRepository.findById(taxId).ifPresent(taxRate -> {
            taxRate.setIsActive(isActive);
            taxRateRepository.save(taxRate);
        });
    }
}