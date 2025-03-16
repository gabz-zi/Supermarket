package org.example.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {
    private final String name;
    private BigDecimal budget;
    private final Map<Product, Integer> basket = new HashMap<>();

    public Customer(String name, BigDecimal budget) {
        this.name = name;
        this.budget = budget;
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
        this.budget = budget;
    }
}

