package org.example.repositories.legacy;

import org.example.models.legacy.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String> {

    Shop findByName(String name);

}
