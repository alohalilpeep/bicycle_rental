package org.example.repositories;

import org.example.models.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, String> {
    List<TaxRate> findByIsActiveTrue();
    TaxRate findByTaxName(String taxName);
}