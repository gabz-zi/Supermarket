package org.example.interfaces;

import org.example.services.ProductService;
import java.math.BigDecimal;


public interface Sellable {
    BigDecimal getSellingPrice(ProductService productService);
}
