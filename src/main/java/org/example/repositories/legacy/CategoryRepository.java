package org.example.repositories.legacy;

import org.example.models.legacy.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{
    Category findByName(String name);
}
