package org.example;

import org.example.models.*;
import org.example.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

public class Main {
    private static final String PATH_TO_RECEIPTS = "C:\\Users\\Vlad\\IdeaProjects\\ReweSupermarket\\";

    public static void main(String[] args) {
        // Create a Shop
        Shop shop = new Shop("SuperMart", BigDecimal.valueOf(20), BigDecimal.valueOf(50));
        ProductService productService = new ProductService(shop);
        ReceiptService receiptService = new ReceiptService(productService);
        CashDesk cashDesk = new CashDesk();
        Cashier cashier = new Cashier("pepo", cashDesk, BigDecimal.TEN);
        ShopService shopService = new ShopService(productService);
        shopService.addCashier(shop, cashier);
        CustomerService customerService = new CustomerService();
        CashDeskService cashDeskService = new CashDeskService();

        CashierService cashierService = new CashierService(receiptService, shopService, productService
                , customerService, cashDeskService);


        // Create products
        Product milk = new FoodProduct("Milk", BigDecimal.valueOf(5.00), LocalDate.now().plusDays(5), productService); // Expires in 5 days
        Product shampoo = new NonFoodProduct("Shampoo", BigDecimal.valueOf(10.00)); // No expiry date

        // Add stock (with delivery costs)
        shopService.addStock(shop, milk, 10);
        shopService.addStock(shop, shampoo, 5);

        // Check stock before selling
        System.out.println("Stock before selling:");
        shop.getStock().forEach((product, quantity) ->
                System.out.println(product.getName() + " - " + quantity));

        // Sell some products
        Customer customer = new Customer("Petio", BigDecimal.valueOf(100));
        customerService.addToBasket(customer, milk, 2);
        customerService.addToBasket(customer, shampoo, 1);
        cashDeskService.addCustomerToQueue(customer, cashDesk);
        Receipt receipt = cashierService.processPurchase(customer, cashier, shop);

        //  shopService.sellProduct(milk, 2);
        //  shopService.sellProduct(shampoo, 1);

        // Check stock after selling
        System.out.println("\nStock after selling:");
        shop.getStock().forEach((product, quantity) ->
                System.out.println(product.getName() + " - " + quantity));

        // Calculate and display shop's profit
        BigDecimal profit = shopService.calculateProfit(shop);
        System.out.println("\nTotal Profit: " + profit);

        //check if deserialization went smoothly
        Receipt receiptLoaded = receiptService.loadFromFile
                (String.format("%sreceipt_%s.txt", PATH_TO_RECEIPTS, receipt.getId()));

        System.out.println(String.format("Receipt ID: %s", receiptLoaded.getId()));

        Cashier cashier1 = shopService.getCashiers(shop).stream()
                .filter(c -> c.getId().equals(receiptLoaded.getCashierId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found"));
        System.out.println(String.format("Cashier on shift: %s", cashier1.getName()));

        System.out.println("Products sold:");
        Map<Product, Integer> productsMap = receiptLoaded.getProducts();

        for (Map.Entry<Product, Integer> entry : productsMap.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            // Assuming Product has a getName() and getPrice() method (you can adjust as per your Product class structure)
            System.out.println("Product: " + product.getName() + ", Quantity: " + quantity + ", Price: " + product.getSellingPrice(productService));
        }

        System.out.println(String.format("Total payed: %.2f", receiptLoaded.getTotalAmount()));
        System.out.println(String.format("Date of purchase: %s", receiptLoaded.getDate()));
    }
}

