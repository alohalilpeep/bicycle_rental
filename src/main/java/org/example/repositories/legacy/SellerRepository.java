package org.example.repositories.legacy;

import org.example.models.legacy.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {

    Seller findByFirstNameAndLastName(String firstName, String lastName);
}
