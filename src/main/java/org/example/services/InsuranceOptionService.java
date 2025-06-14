package org.example.services;

import org.example.models.InsuranceOption;
import org.example.services.dto.InsuranceOptionDto;

import java.util.List;

public interface InsuranceOptionService {
    void createInsuranceOption(InsuranceOptionDto insuranceOptionDto);

    List<InsuranceOption> getAllActiveInsuranceOptions();

    void toggleInsuranceOptionStatus(String id, boolean isActive);
}
