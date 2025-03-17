package org.example;

import org.example.ProductCategory;
import org.example.exceptions.SellingExpiredProductException;
import org.example.interfaces.Expirable;
import org.example.models.FoodProduct;
import org.example.models.Product;
import org.example.models.Shop;
import org.example.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService productService;
    private Shop shop;
    private Product product;

    @BeforeEach
    public void setUp() {
        productService = new ProductService();
        shop = mock(Shop.class);
        product = mock(Product.class);
    }

    @Test
    public void testCalculatePrice_noExpiry_noDiscount() {
        // Setup
        BigDecimal basePrice = BigDecimal.valueOf(100);
        when(product.getBasePrice()).thenReturn(basePrice);
        when(product.getCategory()).thenReturn(ProductCategory.NON_FOOD);
        when(shop.getMarkup(ProductCategory.NON_FOOD)).thenReturn(BigDecimal.valueOf(10));

        // Call method
        BigDecimal price = productService.calculatePrice(shop, product);

        // Verify
        BigDecimal expectedPrice = basePrice.add(basePrice.multiply(BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100))));
        assertEquals(expectedPrice, price);
    }

    @Test
    public void testCalculatePrice_expiredProduct() {
        // Setup expired product
        FoodProduct expiredProduct = mock(FoodProduct.class);
        BigDecimal basePrice = BigDecimal.valueOf(100);
        when(expiredProduct.getBasePrice()).thenReturn(basePrice);
        when(expiredProduct.getCategory()).thenReturn(ProductCategory.FOOD);
        when(expiredProduct.getExpireDate()).thenReturn(LocalDate.now().minusDays(1));
        when(shop.getMarkup(ProductCategory.FOOD)).thenReturn(BigDecimal.valueOf(10));


        // Verify exception
        assertThrows(SellingExpiredProductException.class, () -> {
            productService.calculatePrice(shop, expiredProduct);
        });
    }

    @Test
    public void testCalculatePrice_closeToExpiry_withDiscount() {
        // Setup product near expiry
        FoodProduct expirableProduct = mock(FoodProduct.class); // Mock an actual Expirable subclass
        LocalDate expireDate = LocalDate.now().plusDays(2); // 2 days before expiry

        // Mock required methods
        when(expirableProduct.getExpireDate()).thenReturn(expireDate);
        when(expirableProduct.getBasePrice()).thenReturn(BigDecimal.valueOf(100));
        when(expirableProduct.getCategory()).thenReturn(ProductCategory.FOOD);

        // Mock shop behavior
        when(shop.getMarkup(ProductCategory.FOOD)).thenReturn(BigDecimal.valueOf(10)); // 10% markup
        when(shop.getExpiryDiscount()).thenReturn(BigDecimal.valueOf(20)); // 20% discount
        when(shop.getDaysBeforeExpiryDiscount()).thenReturn(3); // Discount applies 3 days before expiry

        // Call method
        BigDecimal price = productService.calculatePrice(shop, expirableProduct);

        // Verify expected price calculation
        BigDecimal basePrice = BigDecimal.valueOf(100);
        BigDecimal markupAmount = basePrice.multiply(BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100))); // 10% markup
        BigDecimal finalPriceBeforeDiscount = basePrice.add(markupAmount);
        BigDecimal discountAmount = finalPriceBeforeDiscount.multiply(BigDecimal.valueOf(20).divide(BigDecimal.valueOf(100))); // 20% discount
        BigDecimal expectedPrice = finalPriceBeforeDiscount.subtract(discountAmount);

        assertEquals(expectedPrice, price);
    }


    @Test
    public void testCalculatePrice_noExpiryProduct() {
        // Setup non-expirable product (mocking Product directly)
        Product nonExpirableProduct = mock(Product.class);

        BigDecimal basePrice = BigDecimal.valueOf(100);
        when(nonExpirableProduct.getBasePrice()).thenReturn(basePrice);
        when(nonExpirableProduct.getCategory()).thenReturn(ProductCategory.FOOD);
        when(shop.getMarkup(ProductCategory.FOOD)).thenReturn(BigDecimal.valueOf(10));

        // Call method
        BigDecimal price = productService.calculatePrice(shop, nonExpirableProduct);

        // Verify expected price
        BigDecimal expectedPrice = basePrice.add(basePrice.multiply(BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100))));
        assertEquals(expectedPrice, price);
    }


    @Test
    public void testIsExpired() {
        // Test expired product
        Expirable expiredProduct = mock(Expirable.class);
        when(expiredProduct.getExpireDate()).thenReturn(LocalDate.of(2022, Month.JANUARY, 1));

        // Verify
        assertTrue(productService.isExpired(expiredProduct));
    }

    @Test
    public void testIsNotExpired() {
        // Test non-expired product
        Expirable validProduct = mock(Expirable.class);
        when(validProduct.getExpireDate()).thenReturn(LocalDate.of(2026, Month.MARCH, 16));

        // Verify
        assertFalse(productService.isExpired(validProduct));
    }
}
