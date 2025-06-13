package org.example.services;

import org.example.services.dto.SellerDto;

public interface SellerService {

    void addSeller(SellerDto sellerDto);

    void addManager(String[] sellerParams, String[] managerParams);
}
