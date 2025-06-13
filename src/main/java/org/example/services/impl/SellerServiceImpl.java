package org.example.services.impl;

import org.example.models.legacy.Seller;
import org.example.repositories.legacy.SellerRepository;
import org.example.services.SellerService;
import org.example.services.ShopService;
import org.example.services.dto.SellerDto;
import org.example.util.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;
    private ShopService shopService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public SellerServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void addSeller(SellerDto sellerDto) {

        if (!this.validationUtil.isValid(sellerDto)) {

            this.validationUtil
                    .violations(sellerDto)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        Seller seller = this.modelMapper.map(sellerDto, Seller.class);
        seller.setShop(shopService.findShopByName(sellerDto.getShop()));

        this.sellerRepository.saveAndFlush(seller);
    }

    @Override
    public void addManager(String[] sellerParams, String[] managerParams) {
        Seller seller = this.sellerRepository
                .findByFirstNameAndLastName(sellerParams[0], sellerParams[1]);

        Seller manager = this.sellerRepository
                .findByFirstNameAndLastName(managerParams[0], managerParams[1]);

        seller.setManager(manager);

        sellerRepository.saveAndFlush(seller);
    }

    @Autowired
    public void setSellerRepository(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Autowired
    public void setShopService(ShopService shopService) {
        this.shopService = shopService;
    }
}
