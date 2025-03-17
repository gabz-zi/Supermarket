package org.example;

import org.example.exceptions.CustomerInsufficientFundsException;
import org.example.exceptions.OutOfStockException;
import org.example.exceptions.ProductNotInBasketException;
import org.example.models.Customer;
import org.example.models.Product;
import org.example.models.Shop;
import org.example.services.CustomerService;
import org.example.services.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private ShopService shopService;
    private CustomerService customerService;
    private Customer customer;
    private Product product;
    private Shop shop;

    @BeforeEach
    void setUp() {
        shopService = mock(ShopService.class);
        customerService = new CustomerService(shopService);
        customer = mock(Customer.class);
        product = mock(Product.class);
        shop = mock(Shop.class);
    }

    // --- Test addToBasket() ---
    @Test
    void testAddToBasket_Success() {
        // Setup mock responses
        when(shopService.isInStock(shop, product, 2)).thenReturn(true);
        Map<Product, Integer> basket = new HashMap<>();
        when(customer.getBasket()).thenReturn(basket);

        // Call method
        customerService.addToBasket(shop, customer, product, 2);

        // Verify product was added
        assertEquals(2, basket.get(product));
    }

    @Test
    void testAddToBasket_ThrowsOutOfStockException() {
        when(shopService.isInStock(shop, product, 2)).thenReturn(false);

        assertThrows(OutOfStockException.class, () -> {
            customerService.addToBasket(shop, customer, product, 2);
        });
    }

    // --- Test removeFromBasket() ---
    @Test
    void testRemoveFromBasket_Success_DecreasesQuantity() {
        Map<Product, Integer> basket = new HashMap<>();
        basket.put(product, 5);
        when(customer.getBasket()).thenReturn(basket);

        customerService.removeFromBasket(customer, product, 2);

        assertEquals(3, basket.get(product));  // Quantity should decrease
    }

    @Test
    void testRemoveFromBasket_Success_RemovesProduct() {
        Map<Product, Integer> basket = new HashMap<>();
        basket.put(product, 2);
        when(customer.getBasket()).thenReturn(basket);

        customerService.removeFromBasket(customer, product, 2);

        assertFalse(basket.containsKey(product));  // Product should be removed
    }

    @Test
    void testRemoveFromBasket_ThrowsProductNotInBasketException() {
        Map<Product, Integer> basket = new HashMap<>();
        when(customer.getBasket()).thenReturn(basket);

        assertThrows(ProductNotInBasketException.class, () -> {
            customerService.removeFromBasket(customer, product, 1);
        });
    }

    // --- Test canAfford() ---
    @Test
    void testCanAfford_True() {
        when(customer.getBudget()).thenReturn(new BigDecimal("100"));

        assertTrue(customerService.canAfford(customer, new BigDecimal("50")));
    }

    @Test
    void testCanAfford_False() {
        when(customer.getBudget()).thenReturn(new BigDecimal("30"));

        assertFalse(customerService.canAfford(customer, new BigDecimal("50")));
    }

    // --- Test deduct() ---
    @Test
    void testDeduct_Success() {
        when(customer.getBudget()).thenReturn(new BigDecimal("100"));

        customerService.deduct(customer, new BigDecimal("50"));

        verify(customer).setBudget(new BigDecimal("50"));  // Ensure budget is updated
    }

    @Test
    void testDeduct_ThrowsCustomerInsufficientFundsException() {
        when(customer.getBudget()).thenReturn(new BigDecimal("30"));

        assertThrows(CustomerInsufficientFundsException.class, () -> {
            customerService.deduct(customer, new BigDecimal("50"));
        });
    }
}

