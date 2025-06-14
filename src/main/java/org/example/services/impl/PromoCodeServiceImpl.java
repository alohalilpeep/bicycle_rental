package org.example.services.impl;

import org.example.models.PromoCode;
import org.example.repositories.PromoCodeRepository;
import org.example.services.PromoCodeService;
import org.example.services.dto.PromoCodeDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public PromoCodeServiceImpl(PromoCodeRepository promoCodeRepository,
                                ValidationUtil validationUtil,
                                ModelMapper modelMapper) {
        this.promoCodeRepository = promoCodeRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createPromoCode(PromoCodeDto promoCodeDto) {
        if (!validationUtil.isValid(promoCodeDto)) {
            validationUtil.violations(promoCodeDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            return;
        }

        if (promoCodeRepository.findByCode(promoCodeDto.getCode()).isPresent()) {
            System.out.println("Promo code already exists");
            return;
        }

        PromoCode promoCode = modelMapper.map(promoCodeDto, PromoCode.class);
        promoCodeRepository.save(promoCode);
    }

    @Override
    public boolean validatePromoCode(String code) {
        Optional<PromoCode> promoCode = promoCodeRepository.findByCode(code);
        return promoCode.isPresent()
                && promoCode.get().getIsActive()
                && LocalDate.now().isAfter(promoCode.get().getValidFrom())
                && LocalDate.now().isBefore(promoCode.get().getValidTo())
                && (promoCode.get().getMaxUses() == null
                || promoCode.get().getCurrentUses() < promoCode.get().getMaxUses());
    }
}