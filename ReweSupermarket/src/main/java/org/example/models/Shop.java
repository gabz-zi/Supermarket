package org.example.models;

import org.example.ProductCategory;

import java.math.BigDecimal;
import java.util.*;

public class Shop {
    private final String name;
    private Map<Product, Integer> stock; // Tracks product quantities
    private final Map<ProductCategory, BigDecimal> markupPercentages;
    private BigDecimal expiryDiscount;
    private int daysBeforeExpiryDiscount;
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalDeliveryCosts = BigDecimal.ZERO;
    private List<Cashier> cashiers;

    public Shop(String name, BigDecimal foodMarkup, BigDecimal nonFoodMarkup) {
        markupPercentages = new EnumMap<>(ProductCategory.class);
        markupPercentages.put(ProductCategory.FOOD, foodMarkup);
        markupPercentages.put(ProductCategory.NON_FOOD, nonFoodMarkup);
        this.expiryDiscount = BigDecimal.valueOf(50); // 50% discount for near-expiry products
        this.daysBeforeExpiryDiscount = 3; // Discount triggers applies 3 days before expiry
        this.name = name;
        this.stock = new HashMap<>();
        cashiers = new ArrayList<>();
    }

    public void addIncome(BigDecimal amount) { totalIncome = totalIncome.add(amount); }
    public void addDeliveryCost(BigDecimal cost) { totalDeliveryCosts = totalDeliveryCosts.add(cost); }

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
        this.daysBeforeExpiryDiscount = daysBeforeExpiryDiscount;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public void setCashiers(List<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public void setExpiryDiscount(BigDecimal expiryDiscount) {
        this.expiryDiscount = expiryDiscount;
    }
}
