package org.example.services.impl;

import org.example.models.*;
import org.example.repositories.PaymentMethodRepository;
import org.example.repositories.TransactionRepository;
import org.example.repositories.UserRepository;
import org.example.services.PaymentService;
import org.example.services.dto.PaymentMethodDto;
import org.example.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public PaymentServiceImpl(PaymentMethodRepository paymentMethodRepository,
                              UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              ValidationUtil validationUtil,
                              ModelMapper modelMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addPaymentMethod(PaymentMethodDto paymentMethodDto) {
        if (!validationUtil.isValid(paymentMethodDto)) {
            validationUtil.violations(paymentMethodDto)
                    .forEach(v -> System.out.println(v.getMessage()));
            return;
        }

        User user = userRepository.findById(paymentMethodDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);
        paymentMethod.setUser(user);

        paymentMethod.setIsDefault(paymentMethodDto.isDefault());

        if (paymentMethodDto.isDefault()) {
            paymentMethodRepository.findByUserAndIsDefaultTrue(user)
                    .forEach(pm -> {
                        pm.setIsDefault(false);
                        paymentMethodRepository.save(pm);
                    });
        }

        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public List<PaymentMethod> getUserPaymentMethods(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return paymentMethodRepository.findByUser(user);
    }
}