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
        shop.addDeliveryCost(deliveryCost);
    }

    private boolean isInStock(Shop shop, Product product, int quantity) {
        return shop.getStock().getOrDefault(product, 0) >= quantity;
    }


    public void sellProduct(Shop shop, Product product, int quantity) {
        if (!isInStock(shop, product, quantity)) {
            throw new OutOfStockException("Not enough stock for " + product.getName());
        }
        if (product instanceof Expirable && (productService.isExpired((Expirable) product))) {
            throw new ExpiredProductException("Cannot sell expired product: " + product.getName());
        }
        BigDecimal totalPrice = product.getSellingPrice(productService).multiply(BigDecimal.valueOf(quantity));
        shop.addIncome(totalPrice);

        shop.getStock().put(product, shop.getStock().get(product) - quantity);
    }

    public BigDecimal calculateProfit(Shop shop) {
        return shop.getTotalIncome()
                .subtract(shop.getTotalDeliveryCosts())
                .subtract(calculateExpensesForSalaries(shop));
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
