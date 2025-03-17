package org.example.models;

import org.example.ProductCategory;
import org.example.exceptions.InvalidDaysBeforeExpiryDiscountException;
import org.example.exceptions.InvalidExpiryDiscountException;
import org.example.exceptions.InvalidMarkupPercentagesException;
import org.example.exceptions.InvalidShopNameException;

import java.math.BigDecimal;
import java.util.*;

public class Shop {
    private final String name;
    private Map<Product, Integer> stock;
    private Map<ProductCategory, BigDecimal> markupPercentages;
    private BigDecimal expiryDiscount;
    private int daysBeforeExpiryDiscount;
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalDeliveryCosts = BigDecimal.ZERO;
    private Set<Cashier> cashiers;

    public Shop(String name, BigDecimal foodMarkup, BigDecimal nonFoodMarkup, BigDecimal expiryDiscount, int daysBeforeExpiryDiscount) {
        if (name == null || name.isBlank()) throw new InvalidShopNameException("Shop name cannot be empty.");
        this.markupPercentages = fillEnumMapForMarkupPercentages(foodMarkup, nonFoodMarkup);
        this.setExpiryDiscount(expiryDiscount);
        this.setDaysBeforeExpiryDiscount(daysBeforeExpiryDiscount);
        this.name = name;
        this.stock = new HashMap<>();
        cashiers = new HashSet<>();
        //HashSet doesn't allow duplicates → No need to manually check if a cashier is already in the shop when calling the shpService add method.
        //Faster add() and contains() checks → O(1) in HashSet.
        //Iteration for salary calculations is still O(n) → No performance loss compared to if we've used a list.
        //since we don't need to keep an order, we use just HashSet
    }

    private Map<ProductCategory, BigDecimal> fillEnumMapForMarkupPercentages(BigDecimal foodMarkup, BigDecimal nonFoodMarkup) {
        if (foodMarkup.compareTo(BigDecimal.ZERO) != 1 || nonFoodMarkup.compareTo(BigDecimal.ZERO) != 1) {
            throw new InvalidMarkupPercentagesException("Markup for food and non-food" +
                    "products must be a positive number.");
        }
        markupPercentages = new EnumMap<>(ProductCategory.class);
        markupPercentages.put(ProductCategory.FOOD, foodMarkup);
        markupPercentages.put(ProductCategory.NON_FOOD, nonFoodMarkup);
        return markupPercentages;
    }
    public BigDecimal getMarkup(ProductCategory category) {
        return markupPercentages.get(category);
    }

    public BigDecimal getExpiryDiscount() {
        return expiryDiscount;
    }

    public int getDaysBeforeExpiryDiscount() {
        return daysBeforeExpiryDiscount;
    }

    public String getName() {
        return name;
    }

    public Map<Product, Integer> getStock() {
        return stock; //should be unmodifiable
    }

    public void setStock(Map<Product, Integer> stock) {
        this.stock = stock;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalDeliveryCosts() {
        return totalDeliveryCosts;
    }

    public void setTotalDeliveryCosts(BigDecimal totalDeliveryCosts) {
        this.totalDeliveryCosts = totalDeliveryCosts;
    }


    public void setDaysBeforeExpiryDiscount(int daysBeforeExpiryDiscount) {
        if (expiryDiscount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDaysBeforeExpiryDiscountException("Days before expiry discount is applied" +
                    " must be a positive number.");
        }
        this.daysBeforeExpiryDiscount = daysBeforeExpiryDiscount;
    }

    public Set<Cashier> getCashiers() {
        return cashiers;
    }

    public void setCashiers(Set<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public void setExpiryDiscount(BigDecimal expiryDiscount) {
        if (expiryDiscount.compareTo(BigDecimal.ZERO) != 1) {
            throw new InvalidExpiryDiscountException("Expiry discount must be a positive number.");
        }
        this.expiryDiscount = expiryDiscount;
    }
}
