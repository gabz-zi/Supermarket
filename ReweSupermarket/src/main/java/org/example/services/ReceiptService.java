package org.example.services;

import org.example.models.Cashier;
import org.example.models.Customer;
import org.example.models.Product;
import org.example.models.Receipt;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReceiptService {
    private final ProductService productService;

    public ReceiptService(ProductService productService) {
        this.productService = productService;
    }

    public Receipt createReceipt(Customer customer, Cashier cashier) {
        BigDecimal totalAmount = calculateTotal(customer.getBasket());
        Receipt receipt = new Receipt(customer, cashier.getId(), totalAmount);
        saveToFile(receipt);
        return receipt;
    }

    private void saveToFile(Receipt receipt) {
        String filename = "receipt_" + receipt.getId() + ".txt";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal calculateTotal(Map<Product, Integer> products) {
        return products.entrySet().stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    Integer quantity = entry.getValue();
                    BigDecimal productPrice = productService.calculatePrice(product);
                    return productPrice.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Receipt loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Receipt) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
