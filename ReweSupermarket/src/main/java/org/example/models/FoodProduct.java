package org.example.models;

import org.example.interfaces.Expirable;
import org.example.ProductCategory;
import org.example.services.ProductService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FoodProduct extends Product implements Expirable {
    private final LocalDate expireDate;
    // once produced a product's expirationDate cannot and should not change
    //It is agreed on that all food products have expireDate, even honey, as it has best before label which
    //means that with time its benefits vanish or the products is not as good as it should be

    public FoodProduct(String name, BigDecimal basePrice, LocalDate expireDate, ProductService productService) {
        super(name, basePrice, ProductCategory.FOOD);
        this.expireDate = expireDate;
    }

    @Override
    public LocalDate getExpireDate() {
        return this.expireDate;
    }

}
