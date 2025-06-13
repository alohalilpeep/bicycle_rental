package org.example.repositories.legacy;

import org.example.models.legacy.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, String> {

    Optional<Town> findByName(String name);
}
