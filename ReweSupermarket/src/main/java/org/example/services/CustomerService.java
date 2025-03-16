package org.example.services;

import org.example.models.Customer;
import org.example.models.Product;

import java.math.BigDecimal;

public class CustomerService {

    public void addToBasket(Customer customer, Product product, int quantity) {
        customer.getBasket().put(product, customer.getBasket().getOrDefault(product, 0) + quantity);
    }

    public boolean canAfford(Customer customer, BigDecimal amount) {
        return customer.getBudget().compareTo(amount) >= 0;
    }

    public void deduct(Customer customer, BigDecimal amount) {
        if (canAfford(customer, amount)) {
            customer.setBudget(customer.getBudget().subtract(amount));
        } else {
            throw new IllegalArgumentException("Not enough money");
        }
    }
}
