package org.example.services.impl;

import org.example.models.PaymentMethod;
import org.example.models.User;
import org.example.repositories.PaymentMethodRepository;
import org.example.repositories.TransactionRepository;
import org.example.repositories.UserRepository;
import org.example.services.dto.PaymentMethodDto;
import org.example.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void addPaymentMethod_WithInvalidDto_ShouldNotProceed() {
        // Arrange
        PaymentMethodDto invalidDto = new PaymentMethodDto();
        when(validationUtil.isValid(invalidDto)).thenReturn(false);

        // Act
        paymentService.addPaymentMethod(invalidDto);

        // Assert
        verify(validationUtil).violations(invalidDto);
        verifyNoInteractions(userRepository, modelMapper, paymentMethodRepository);
    }

    @Test
    void addPaymentMethod_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        PaymentMethodDto validDto = createValidDto();
        when(validationUtil.isValid(validDto)).thenReturn(true);
        when(userRepository.findById("user-123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                paymentService.addPaymentMethod(validDto));
        verifyNoInteractions(modelMapper, paymentMethodRepository);
    }

    @Test
    void addPaymentMethod_WithValidDto_ShouldSavePaymentMethod() {
        // Arrange
        PaymentMethodDto dto = createValidDto();
        User user = new User();
        PaymentMethod paymentMethod = new PaymentMethod();

        when(validationUtil.isValid(dto)).thenReturn(true);
        when(userRepository.findById("user-123")).thenReturn(Optional.of(user));
        when(modelMapper.map(dto, PaymentMethod.class)).thenReturn(paymentMethod);

        // Act
        paymentService.addPaymentMethod(dto);

        // Assert
        assertEquals(user, paymentMethod.getUser(), "User should be set");
        verify(paymentMethodRepository).save(paymentMethod);
    }

    @Test
    void addPaymentMethod_WhenSettingDefault_ShouldUpdateExistingDefaults() {
        // Arrange
        PaymentMethodDto dto = createValidDto();
        dto.setDefault(true);

        User user = new User();
        PaymentMethod existingDefault = new PaymentMethod();
        existingDefault.setIsDefault(true);
        PaymentMethod newPaymentMethod = new PaymentMethod();
        newPaymentMethod.setIsDefault(true); // Explicitly set expected state

        when(validationUtil.isValid(dto)).thenReturn(true);
        when(userRepository.findById("user-123")).thenReturn(Optional.of(user));
        when(modelMapper.map(dto, PaymentMethod.class)).thenReturn(newPaymentMethod);
        when(paymentMethodRepository.findByUserAndIsDefaultTrue(user))
                .thenReturn(List.of(existingDefault));

        // Act
        paymentService.addPaymentMethod(dto);

        // Assert
        assertFalse(existingDefault.getIsDefault(), "Existing default should be deactivated");
        assertTrue(newPaymentMethod.getIsDefault(), "New method should be default");
        verify(paymentMethodRepository).save(existingDefault);
        verify(paymentMethodRepository).save(newPaymentMethod);
    }

    @Test
    void addPaymentMethod_WhenNoExistingDefaults_ShouldSetNewAsDefault() {
        // Arrange
        PaymentMethodDto dto = createValidDto();
        dto.setDefault(true);

        User user = new User();
        PaymentMethod newPaymentMethod = new PaymentMethod();
        newPaymentMethod.setIsDefault(true); // Explicitly set expected state

        when(validationUtil.isValid(dto)).thenReturn(true);
        when(userRepository.findById("user-123")).thenReturn(Optional.of(user));
        when(modelMapper.map(dto, PaymentMethod.class)).thenReturn(newPaymentMethod);
        when(paymentMethodRepository.findByUserAndIsDefaultTrue(user))
                .thenReturn(Collections.emptyList());

        // Act
        paymentService.addPaymentMethod(dto);

        // Assert
        assertTrue(newPaymentMethod.getIsDefault(), "New method should be default");
        verify(paymentMethodRepository).save(newPaymentMethod);
    }

    @Test
    void addPaymentMethod_WhenNotDefault_ShouldNotUpdateExistingDefaults() {
        // Arrange
        PaymentMethodDto dto = createValidDto();
        dto.setDefault(false);

        User user = new User();
        PaymentMethod newPaymentMethod = new PaymentMethod();

        when(validationUtil.isValid(dto)).thenReturn(true);
        when(userRepository.findById("user-123")).thenReturn(Optional.of(user));
        when(modelMapper.map(dto, PaymentMethod.class)).thenReturn(newPaymentMethod);

        // Act
        paymentService.addPaymentMethod(dto);

        // Assert
        assertFalse(newPaymentMethod.getIsDefault(), "New method should not be default");
        verify(paymentMethodRepository).save(newPaymentMethod);
        verify(paymentMethodRepository, never()).findByUserAndIsDefaultTrue(any());
    }

    @Test
    void getUserPaymentMethods_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById("user-404")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                paymentService.getUserPaymentMethods("user-404"));
    }

    @Test
    void getUserPaymentMethods_WhenUserExists_ShouldReturnMethods() {
        // Arrange
        User user = new User();
        PaymentMethod method1 = new PaymentMethod();
        PaymentMethod method2 = new PaymentMethod();

        when(userRepository.findById("user-123")).thenReturn(Optional.of(user));
        when(paymentMethodRepository.findByUser(user)).thenReturn(List.of(method1, method2));

        // Act
        List<PaymentMethod> result = paymentService.getUserPaymentMethods("user-123");

        // Assert
        assertEquals(2, result.size(), "Should return user's payment methods");
    }

    private PaymentMethodDto createValidDto() {
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setUserId("user-123");
        dto.setMethodType("CREDIT_CARD");
        dto.setDetails("encrypted-data");
        dto.setDefault(false);
        return dto;
    }
}