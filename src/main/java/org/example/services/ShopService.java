package org.example.services;

import org.example.models.legacy.Shop;
import org.example.services.dto.ShopDto;
import org.example.views.ProductViewModel;
import org.example.views.SellerViewModel;

import java.util.List;

public interface ShopService {

    void addShop(ShopDto shopDto);

    Shop findShopByName(String name);

    List<SellerViewModel> findAllSellersFromShop(String shopName);

    List<ProductViewModel> findAllProductsFromShop(String shopName);
}
