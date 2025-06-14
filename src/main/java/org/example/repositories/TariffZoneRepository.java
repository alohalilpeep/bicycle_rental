package org.example.repositories;

import org.example.models.TariffZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TariffZoneRepository extends JpaRepository<TariffZone, String> {

    @Override
    Optional<TariffZone> findById(String id);

    Optional<TariffZone> findByZoneName(String zoneName);

    @Override
    List<TariffZone> findAll();

    @Override
    <S extends TariffZone> S save(S entity);

}