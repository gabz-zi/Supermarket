package org.example.models;
import org.example.ProductCategory;
import org.example.exceptions.InvalidProductBasePriceException;
import org.example.exceptions.InvalidProductNameException;
import org.example.interfaces.Expirable;
import org.example.interfaces.Sellable;
import org.example.services.ProductService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Product implements Serializable {
    private final String id;
    private final String name;
    private BigDecimal basePrice;
    // can change cuz of inflation
    private final ProductCategory category;
    //a product is either food or nonFood in nature, not changeable

    public Product(String name, BigDecimal basePrice, ProductCategory category) {
        this.id = UUID.randomUUID().toString();
        if (name == null || name.isBlank()) throw new InvalidProductNameException("Product name cannot be empty.");
        if (basePrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidProductBasePriceException("Product base price must be positive.");
        this.name = name;
        this.basePrice = basePrice;
        this.category = category;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }


}