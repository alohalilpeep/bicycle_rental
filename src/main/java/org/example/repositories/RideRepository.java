package org.example.repositories;

import org.example.models.Ride;
import org.example.models.User;
import org.example.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, String> {
    List<Ride> findByUser(User user);
    List<Ride> findByPaymentStatus(PaymentStatus status);
    List<Ride> findByUserAndPaymentStatus(User user, PaymentStatus status);
}