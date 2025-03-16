package org.example.models;

import org.example.ProductCategory;
import org.example.interfaces.Sellable;
import org.example.services.ProductService;

import java.math.BigDecimal;
import java.util.EnumMap;

public class NonFoodProduct extends Product {
    public NonFoodProduct(String name, BigDecimal basePrice) {
        super(name, basePrice, ProductCategory.NON_FOOD);
    }
}
