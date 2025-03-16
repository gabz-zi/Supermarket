package org.example.models;

import org.example.exceptions.InvalidCashierNameException;
import org.example.exceptions.InvalidCashierSalaryException;
import org.example.exceptions.InvalidCustomerBudgetException;
import org.example.exceptions.InvalidCustomerNameException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {
    private final String name;
    private BigDecimal budget;
    private Map<Product, Integer> basket = new HashMap<>();
    // not final as the customer constantly adds/removes products

    public Customer(String name, BigDecimal budget) {
        if (name == null || name.isBlank()) throw new InvalidCustomerNameException("Customer name cannot be empty.");
        this.name = name;
        this.setBudget(budget);
    }


    public Map<Product, Integer> getBasket() {
        return basket;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        if (budget.compareTo(BigDecimal.ZERO) != 1) {
            throw new InvalidCustomerBudgetException("Customer budget must be a positive number.");
        }
        this.budget = budget;
    }
}

