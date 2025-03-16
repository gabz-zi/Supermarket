package org.example.interfaces;

import org.example.ProductCategory;
import org.example.models.Shop;
import org.example.services.ProductService;

import java.math.BigDecimal;
import java.util.EnumMap;

public interface Sellable {
    BigDecimal getSellingPrice(ProductService productService);
}
