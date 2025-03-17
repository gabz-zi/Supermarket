package org.example;

import org.example.models.*;
import org.example.services.ProductService;
import org.example.services.ReceiptService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceiptServiceTest {
    private ReceiptService receiptService;
    private ProductService productService;
    private Shop shop;
    private Customer customer;
    private Cashier cashier;
    private Product product;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        receiptService = new ReceiptService(productService);

        shop = mock(Shop.class);
        cashier = new Cashier("John Doe", mock(CashDesk.class), new BigDecimal("2000"));
        customer = new Customer("Alice", new BigDecimal("100"));

        product = mock(Product.class);
        when(productService.calculatePrice(shop, product)).thenReturn(new BigDecimal("10"));

        Map<Product, Integer> basket = new HashMap<>();
        basket.put(product, 3); // 3 products at 10 each = 30

        customer.getBasket().putAll(basket);
    }

    @Test
    void testCalculateTotal() {
        BigDecimal total = receiptService.calculateTotal(shop, customer.getBasket());
        assertEquals(new BigDecimal("30"), total, "Total calculation should match expected value.");
    }

    @Test
    void testCreateReceipt() {
        Receipt receipt = receiptService.createReceipt(shop, customer, cashier);
        assertNotNull(receipt, "Receipt should not be null.");
        assertEquals(new BigDecimal("30"), receipt.getTotalAmount(), "Total amount should match calculated total.");
        assertEquals(customer.getBasket(), receipt.getProducts(), "Products in receipt should match customer basket.");
        assertEquals(cashier.getId(), receipt.getCashierId(), "Cashier ID should be set correctly.");
    }

    @Test
    void testSaveAndLoadReceipt() {
        Receipt receipt = receiptService.createReceipt(shop, customer, cashier);
        String filename = "receipt_" + receipt.getId() + ".txt";

        // Load it back
        Receipt loadedReceipt = receiptService.loadFromFile(filename);
        assertNotNull(loadedReceipt, "Loaded receipt should not be null.");
        assertEquals(receipt.getTotalAmount(), loadedReceipt.getTotalAmount(), "Total amount should match.");
        assertEquals(receipt.getCashierId(), loadedReceipt.getCashierId(), "Cashier ID should match.");
    }

    @Test
    void testSaveReceiptIOExceptionHandling() {
        Receipt receipt = new Receipt(customer, cashier.getId(), new BigDecimal("30"));

        // Mocking failure by using an invalid filename
        String invalidFilename = "/invalid/path/receipt.txt";
        try (FileOutputStream fos = new FileOutputStream(invalidFilename)) {
            fail("Expected exception was not thrown.");
        } catch (IOException e) {
            // Expected behavior, do nothing
        }

        receiptService.saveToFile(receipt); // Should print stack trace but not crash
    }



    @Test
    void testLoadReceiptIOExceptionHandling() {
        String nonExistentFile = "nonexistent_receipt.txt";

        // Load non-existing file, should return null but not crash
        Receipt receipt = receiptService.loadFromFile(nonExistentFile);
        assertNull(receipt, "Receipt should be null when loading from a non-existent file.");
    }

    @Test
    void testLoadReceiptCorruptedFile() throws IOException {
        String corruptedFile = "corrupted_receipt.txt";

        // Create a corrupted file with invalid data
        try (FileOutputStream fos = new FileOutputStream(corruptedFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeBytes("corrupted data"); // Not a valid serialized object
        }

        Receipt receipt = receiptService.loadFromFile(corruptedFile);
        assertNull(receipt, "Receipt should be null when loading a corrupted file.");
    }
}
