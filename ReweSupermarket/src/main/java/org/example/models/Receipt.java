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
    //this is final as in the end those are the products that the customer has paid for
    //and if he decides he needs something in the last moment, since he has already paid for the previous items
    //and the receipt has been already issued, he will have to be given a new one
    private final BigDecimal totalAmount;

    //we don't add any validations here as we've checked the customer and cashier before
    //creating the receipt in the processPurchase() method in the CashierService
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

