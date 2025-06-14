package org.example.repositories;

import org.example.models.PaymentMethod;
import org.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    List<PaymentMethod> findByUser(User user);
    List<PaymentMethod> findByUserAndIsDefaultTrue(User user);
}