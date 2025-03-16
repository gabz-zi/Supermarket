package org.example.models;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String cashierId;
    private final LocalDate date;
    private final Map<Product, Integer> products;
    private final BigDecimal totalAmount;

    public Receipt(Customer customer, String cashierId, BigDecimal totalAmount) {
        this.id = UUID.randomUUID().toString();;
        this.cashierId = cashierId;
        this.date = LocalDate.now();
        this.products = customer.getBasket();
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return this.id;
    }

    public String getCashierId() {
        return cashierId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}

