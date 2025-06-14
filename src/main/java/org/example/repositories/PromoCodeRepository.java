package org.example.repositories;

import org.example.models.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
    Optional<PromoCode> findByCode(String code);
    List<PromoCode> findByIsActiveTrueAndValidFromBeforeAndValidToAfter(LocalDate date1, LocalDate date2);
}