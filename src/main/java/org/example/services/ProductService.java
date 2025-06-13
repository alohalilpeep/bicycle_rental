package org.example.services;

import org.example.models.legacy.Product;
import org.example.services.dto.ProductDto;

public interface ProductService {

    void addProduct(ProductDto productDto);

    void addProductDistribution(String productName, String[] shopList);

    Product findByName(String name);
}
