package org.example;

import org.example.services.*;
import org.example.services.dto.ProductDto;
import org.example.services.dto.SellerDto;
import org.example.services.dto.ShopDto;
import org.example.views.ProductViewModel;
import org.example.views.SellerViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class Clr implements CommandLineRunner {

    private final BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));
    private final CategoryService categoryService;
    private final TownService townService;
    private final ShopService shopService;
    private final SellerService sellerService;
    private final ProductService productService;

    @Autowired
    public Clr(CategoryService categoryService, TownService townService, ShopService shopService, SellerService sellerService, ProductService productService) {
        this.categoryService = categoryService;
        this.townService = townService;
        this.shopService = shopService;
        this.sellerService = sellerService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {


        System.out.println("Hi");

        while (true) {
            System.out.println("Choose option from:" +
                    "\n1 - for Add Category" +
                    "\n2 - for Add Town" +
                    "\n3 - for Add Shop" +
                    "\n4 - for Add Seller" +
                    "\n5 - for Add Product" +
                    "\n6 - for Set seller new manager" +
                    "\n7 - for Distributing product in shops" +
                    "\n8 - for Showing all sellers in Shop" +
                    "\n9 - for Showing all products in Shop");


            String input = bufferedReader.readLine();
            if (input == null) {
                // Handle the case where input is null (maybe exit gracefully)
                System.out.println("No input received, exiting...");
                return;
            }
            input = input.toLowerCase();

            switch (input) {
                case "1":
                    this.addCategory();
                    break;
                case "2":
                    this.addTown();
                    break;
                case "3":
                    this.addShop();
                    break;
                case "4":
                    this.addSeller();
                    break;
                case "5":
                    this.addProduct();
                    break;
                case "6":
                    this.setManager();
                    break;
                case "7":
                    this.setProductDistribution();
                    break;
                case "8":
                    this.showAllSellersFromShop();
                    break;
                case "9":
                    this.showAllProductsFromShop();
                    break;
            }
            System.out.println("==================================");
        }
    }

    private void showAllProductsFromShop() throws IOException {
        System.out.println("Enter shop name:");
        String shopName = this.bufferedReader.readLine();

        List<ProductViewModel> productViewModels = this.shopService
                .findAllProductsFromShop(shopName);

        productViewModels.forEach(productViewModel -> {
            System.out.printf("%s - %s $%n", productViewModel.getName(), productViewModel.getPrice());
        });
    }

    private void showAllSellersFromShop() throws IOException {
        System.out.println("Enter shop name:");
        String shopName = this.bufferedReader.readLine();

        List<SellerViewModel> sellerViewModels = this.shopService
                .findAllSellersFromShop(shopName);
        sellerViewModels
                .forEach(s -> System.out.printf("%s %s%n", s.getFirstName(), s.getLastName()));


    }

    private void setProductDistribution() throws IOException {
        System.out.println("Enter product name:");
        String productName = this.bufferedReader.readLine();
        System.out.println("Enter product distribution in " +
                "Shops names in format [shopName1 shopName2 ... ]: ");
        String[] shopList = this.bufferedReader.readLine()
                .split("\\s+");


        try {
            productService.addProductDistribution(productName, shopList);
            System.out.println("Successfully added product distribution!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add product distribution!");
        }
    }

    private void setManager() throws IOException {
        System.out.println("Enter seller first and last names:");
        String[] sellerParams = this.bufferedReader.readLine()
                .split("\\s+");
        System.out.println("Enter manager first and last names:");
        String[] managerParams = this.bufferedReader.readLine()
                .split("\\s+");


        try {
            this.sellerService.addManager(sellerParams, managerParams);
            System.out.println("Successfully added manager!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add manager!");
        }

    }

    private void addProduct() throws IOException {
        System.out.println("Enter product details in format: name price bestBefore(dd-MM-yyyy) category");
        String[] productParams = this.bufferedReader.readLine()
                .split("\\s+");

        ProductDto productDto = new ProductDto();
        productDto.setName(productParams[0]);
        productDto.setPrice(new BigDecimal(productParams[1]));
        productDto.setBestBefore(LocalDate
                .parse(productParams[2], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        productDto.setCategory(productParams[3]);


        try {
            this.productService.addProduct(productDto);
            System.out.println("Successfully added product!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add product!");
        }


    }

    private void addSeller() throws IOException {
        System.out.println("Enter seller details in format: firstName lastName age salary shopName");
        String[] sellerParams = this.bufferedReader.readLine()
                .split("\\s+");

        SellerDto sellerDto = new SellerDto();
        sellerDto.setFirstName(sellerParams[0]);
        sellerDto.setLastName(sellerParams[1]);
        sellerDto.setAge(Integer.valueOf(sellerParams[2]));
        sellerDto.setSalary(new BigDecimal(sellerParams[3]));
        sellerDto.setShop(sellerParams[4]);

        try {
            this.sellerService.addSeller(sellerDto);
            System.out.println("Successfully added seller!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add seller!");
        }
    }

    private void addShop() throws IOException {
        System.out.println("Enter shop details in format: name address town");
        String[] shopParams = this.bufferedReader.readLine()
                .split("\\s+");

        ShopDto shopDto = new ShopDto();
        shopDto.setName(shopParams[0]);
        shopDto.setAddress(shopParams[1]);
        shopDto.setTown(shopParams[2]);

        try {
            this.shopService.addShop(shopDto);
            System.out.println("Successfully added shop!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add shop!");
        }

    }

    private void addTown() throws IOException {
        System.out.println("Enter town name:");
        String townName = this.bufferedReader.readLine();

        try {
            this.townService.addTown(townName);
            System.out.println("Successfully added town!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add town!");
        }


    }

    private void addCategory() throws IOException {
        System.out.println("Enter category name:");
        String categoryName = this.bufferedReader.readLine();

        try {
            this.categoryService.addCategory(categoryName);
            System.out.println("Successfully added category!");
        } catch (Exception e) {
            System.out.println("Error! Cannot add category!");
        }


    }
}