package org.example.services.impl;

import org.example.models.legacy.Town;
import org.example.repositories.legacy.TownRepository;
import org.example.services.TownService;
import org.example.services.dto.TownDto;
import org.example.util.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TownServiceImpl implements TownService {
    private TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public TownServiceImpl(ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addTown(String townName) {
        TownDto townDto = new TownDto();
        townDto.setName(townName);

        if (!this.validationUtil.isValid(townDto)) {

            this.validationUtil
                    .violations(townDto)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        } else {
            this.townRepository
                    .saveAndFlush(this.modelMapper
                            .map(townDto, Town.class));

        }
    }

    @Override
    public Town findTownByName(String townName) {
        return this.townRepository
                .findByName(townName)
                .orElse(null);

    }

    @Autowired
    public void setTownRepository(TownRepository townRepository) {
        this.townRepository = townRepository;
    }
}
