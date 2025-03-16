package org.example.services;

import org.example.exceptions.SellingExpiredProductException;
import org.example.interfaces.Expirable;
import org.example.models.Product;
import org.example.models.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductService {
    public ProductService() {
    }

    public BigDecimal calculatePrice(Shop shop, Product product) {
        BigDecimal basePrice = product.getBasePrice();
        BigDecimal markupPercentage = shop.getMarkup(product.getCategory());
        BigDecimal markupAmount = basePrice.multiply(markupPercentage.divide(BigDecimal.valueOf(100)));

        BigDecimal finalPrice = basePrice.add(markupAmount);

        if (product instanceof Expirable) {
            Expirable expirableProduct = (Expirable) product;

            if (isExpired(expirableProduct)) {
                throw new SellingExpiredProductException("Product is expired and cannot be sold!");
            }

            if (isCloseToExpiry(shop, expirableProduct)) {
                BigDecimal discountPercentage = shop.getExpiryDiscount();
                BigDecimal discountAmount = finalPrice.multiply(discountPercentage.divide(BigDecimal.valueOf(100)));
                finalPrice = finalPrice.subtract(discountAmount);
            }
        }
        return finalPrice;
    }

    private boolean isCloseToExpiry(Shop shop, Expirable product) {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpireDate());
        return daysLeft <= shop.getDaysBeforeExpiryDiscount();
    }

    public boolean isExpired(Expirable product) {
        return LocalDate.now().isAfter(product.getExpireDate());
        // we need a getMethod in the interface here cuz we want every product which has an expiration
        // date to provide information about when is it as it is essential for working with it
    }

}