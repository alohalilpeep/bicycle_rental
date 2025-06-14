package org.example.services;

import org.example.models.PaymentMethod;
import org.example.services.dto.PaymentMethodDto;

import java.util.List;

public interface PaymentService {
    void addPaymentMethod(PaymentMethodDto paymentMethodDto);

    List<PaymentMethod> getUserPaymentMethods(String userId);
}
