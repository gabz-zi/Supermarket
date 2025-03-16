package org.example.models;

import org.example.interfaces.Expirable;
import org.example.services.ProductService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpiringNonFoodProduct extends NonFoodProduct implements Expirable {
    private final LocalDate expireDate;

    public ExpiringNonFoodProduct(String name, BigDecimal basePrice, LocalDate expireDate) {
        super(name, basePrice);
        this.expireDate = expireDate;
    }

    @Override
    public LocalDate getExpireDate() {
        return this.expireDate;
    }

}