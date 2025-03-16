package org.example.services;

import org.example.exceptions.CustomerInsufficientFundsException;
import org.example.exceptions.OutOfStockException;
import org.example.exceptions.ProductNotInBasketException;
import org.example.models.Customer;
import org.example.models.Product;
import org.example.models.Shop;

import java.math.BigDecimal;
import java.util.Map;

public class CustomerService {
   private final ShopService shopService;

    public CustomerService(ShopService shopService) {
        this.shopService = shopService;
    }

    public void addToBasket(Shop shop, Customer customer, Product product, int quantity) {
        if (!shopService.isInStock(shop, product, quantity)) {
            throw new OutOfStockException("Desired product quantity is not in stock.");
        }
        customer.getBasket().put(product, customer.getBasket().getOrDefault(product, 0) + quantity);
    }
    //we don't check if the customer tries to add expired goods to ts basket as it is amely the shop's policy
    //not ot allow the selling of expired products, so at checkout if a customer has picked an expired item
    //they won't be able to proceed wth their purchase

    public void removeFromBasket(Customer customer, Product product, int quantity) {
        Map<Product, Integer> basket = customer.getBasket();
        if (!basket.containsKey(product)) {
            throw new ProductNotInBasketException("Product is not in the customer's basket.");
        }
        int currentQuantity = basket.get(product);
        if (quantity >= currentQuantity) {
            // Remove the product entirely if the requested quantity is equal to or more than what's in the basket
            basket.remove(product);
        } else {
            // Otherwise, just decrease the quantity
            basket.put(product, currentQuantity - quantity);
        }
    }
    public boolean canAfford(Customer customer, BigDecimal amount) {
        return customer.getBudget().compareTo(amount) >= 0;
    }

    public void deduct(Customer customer, BigDecimal amount) {
        if (canAfford(customer, amount)) {
            customer.setBudget(customer.getBudget().subtract(amount));
        } else {
            throw new CustomerInsufficientFundsException("Not enough money, shortage of " + amount.subtract(customer.getBudget()));
        }
    }
}
