package org.example.repositories;

import org.example.models.InsuranceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceOptionRepository extends JpaRepository<InsuranceOption, String> {
    List<InsuranceOption> findByIsActiveTrue();
}