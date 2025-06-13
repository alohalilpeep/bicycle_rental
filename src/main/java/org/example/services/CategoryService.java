package org.example.services;

import org.example.models.legacy.Category;

public interface CategoryService {

    void addCategory(String name);

    Category findByName(String name);
}
