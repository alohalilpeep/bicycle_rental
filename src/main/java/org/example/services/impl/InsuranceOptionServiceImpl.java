package org.example.services.impl;

import org.example.models.InsuranceOption;
import org.example.repositories.InsuranceOptionRepository;
import org.example.services.InsuranceOptionService;
import org.example.services.dto.InsuranceOptionDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceOptionServiceImpl implements InsuranceOptionService {
    private final InsuranceOptionRepository insuranceOptionRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public InsuranceOptionServiceImpl(InsuranceOptionRepository insuranceOptionRepository,
                                      ValidationUtil validationUtil,
                                      ModelMapper modelMapper) {
        this.insuranceOptionRepository = insuranceOptionRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createInsuranceOption(InsuranceOptionDto insuranceOptionDto) {
        if (!validationUtil.isValid(insuranceOptionDto)) {
            validationUtil.violations(insuranceOptionDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            return;
        }

        InsuranceOption insuranceOption = modelMapper.map(insuranceOptionDto, InsuranceOption.class);
        insuranceOptionRepository.save(insuranceOption);
    }

    @Override
    public List<InsuranceOption> getAllActiveInsuranceOptions() {
        return insuranceOptionRepository.findByIsActiveTrue();
    }

    @Override
    public void toggleInsuranceOptionStatus(String id, boolean isActive) {
        insuranceOptionRepository.findById(id).ifPresent(option -> {
            option.setIsActive(isActive);
            insuranceOptionRepository.save(option);
        });
    }
}