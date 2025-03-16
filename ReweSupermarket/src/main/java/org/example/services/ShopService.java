package org.example.services;

import org.example.exceptions.ExpiredProductException;
import org.example.exceptions.OutOfStockException;
import org.example.interfaces.Expirable;
import org.example.models.Cashier;
import org.example.models.Product;
import org.example.models.Shop;

import java.math.BigDecimal;
import java.util.List;


public class ShopService {
    private final ProductService productService;

    public ShopService(ProductService productService) {
        this.productService = productService;
    }


    public void addStock(Shop shop, Product product, int quantity) {
        if (product instanceof Expirable && productService.isExpired((Expirable) product)) {
            throw new ExpiredProductException("Cannot add expired product to stock: " + product.getName());
        }
        shop.getStock().merge(product, quantity, Integer::sum);
        BigDecimal deliveryCost = product.getBasePrice().multiply(BigDecimal.valueOf(quantity));
        addDeliveryCost(shop, deliveryCost);
    } // here the delivery cost is only the basePrice of all products as we supermarkets make giant orders
    //and get discount on delivery services like free delivery

    private void addDeliveryCost(Shop shop, BigDecimal cost) {
        shop.setTotalDeliveryCosts(shop.getTotalDeliveryCosts().add(cost));
    }

    public boolean isInStock(Shop shop, Product product, int quantity) {
        return shop.getStock().getOrDefault(product, 0) >= quantity;
    }

    public void addIncome(Shop shop, BigDecimal amount) {
        shop.setTotalIncome(shop.getTotalIncome().add(amount));
    }

    public void sellProduct(Shop shop, Product product, int quantity) {
        if (!isInStock(shop, product, quantity)) {
            throw new OutOfStockException("Not enough stock for " + product.getName());
        }
        if (product instanceof Expirable && (productService.isExpired((Expirable) product))) {
            throw new ExpiredProductException("Cannot sell expired product: " + product.getName());
        }
        BigDecimal totalPrice = productService.calculatePrice(shop, product).multiply(BigDecimal.valueOf(quantity));
        addIncome(shop, totalPrice);

        shop.getStock().put(product, shop.getStock().get(product) - quantity);
    }

    public BigDecimal calculateProfit(Shop shop) {
        return shop.getTotalIncome()
                .subtract(shop.getTotalDeliveryCosts())
                .subtract(calculateExpensesForSalaries(shop))
                .abs();
    }

    public BigDecimal calculateExpensesForSalaries(Shop shop) {
       return shop.getCashiers().stream()
                .map(Cashier::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addCashier(Shop shop, Cashier cashier) {
        shop.getCashiers().add(cashier);
    }

    public List<Cashier> getCashiers(Shop shop) {
        return shop.getCashiers();
    }
}
