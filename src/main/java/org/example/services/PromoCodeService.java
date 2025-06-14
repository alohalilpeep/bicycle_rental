package org.example.services;

import org.example.services.dto.PromoCodeDto;

public interface PromoCodeService {
    void createPromoCode(PromoCodeDto promoCodeDto);

    boolean validatePromoCode(String code);
}
