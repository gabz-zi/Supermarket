package org.example;

import org.example.exceptions.CashierAlreadyHiredException;
import org.example.exceptions.ExpiredProductException;
import org.example.exceptions.OutOfStockException;
import org.example.interfaces.Expirable;
import org.example.models.*;
import org.example.services.ProductService;
import org.example.services.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopServiceTest {

    private ProductService productService;
    private ShopService shopService;
    private Shop shop;
    private Product product;
    private Cashier cashier;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        shopService = new ShopService(productService);

        shop = mock(Shop.class);
        product = mock(Product.class);
        cashier = mock(Cashier.class);

        when(shop.getStock()).thenReturn(new HashMap<>());
        when(shop.getCashiers()).thenReturn(new HashSet<>());
        when(shop.getTotalDeliveryCosts()).thenReturn(BigDecimal.ZERO);
        when(shop.getTotalIncome()).thenReturn(BigDecimal.ZERO);
    }

    // --- Test addStock() ---
    @Test
    void testAddStock_Success() {
        when(product.getBasePrice()).thenReturn(new BigDecimal("10"));
        when(product.getName()).thenReturn("Test Product");
        when(shop.getTotalDeliveryCosts()).thenReturn(BigDecimal.ZERO);

        shopService.addStock(shop, product, 5);

        assertEquals(5, shop.getStock().get(product));
        verify(shop).setTotalDeliveryCosts(new BigDecimal("50"));
    }

    @Test
    void testAddStock_ThrowsExpiredProductException() {
        ExpiringNonFoodProduct expiredProduct = mock(ExpiringNonFoodProduct.class);
        when(productService.isExpired(expiredProduct)).thenReturn(true);
        when(expiredProduct.getName()).thenReturn("Expired Milk");

        assertThrows(ExpiredProductException.class, () -> {
            shopService.addStock(shop, (Product) expiredProduct, 3);
        });

        assertFalse(shop.getStock().containsKey(expiredProduct)); // Ensure product wasn't added
    }

    // --- Test isInStock() ---
    @Test
    void testIsInStock_True() {
        shop.getStock().put(product, 10);
        assertTrue(shopService.isInStock(shop, product, 5));
    }

    @Test
    void testIsInStock_False() {
        shop.getStock().put(product, 3);
        assertFalse(shopService.isInStock(shop, product, 5));
    }

    // --- Test addIncome() ---
    @Test
    void testAddIncome() {
        when(shop.getTotalIncome()).thenReturn(new BigDecimal("100"));
        shopService.addIncome(shop, new BigDecimal("50"));

        verify(shop).setTotalIncome(new BigDecimal("150"));
    }

    // --- Test sellProduct() ---
    @Test
    void testSellProduct_Success() {
        when(shop.getStock()).thenReturn(new HashMap<>(Map.of(product, 10)));
        when(productService.calculatePrice(shop, product)).thenReturn(new BigDecimal("15"));
        when(product.getName()).thenReturn("Product A");

        shopService.sellProduct(shop, product, 2);

        assertEquals(8, shop.getStock().get(product));
        verify(shop).setTotalIncome(new BigDecimal("30"));  // 2 * 15
    }

    @Test
    void testSellProduct_ThrowsOutOfStockException() {
        when(shop.getStock()).thenReturn(new HashMap<>(Map.of(product, 1)));
        when(product.getName()).thenReturn("Limited Item");

        assertThrows(OutOfStockException.class, () -> {
            shopService.sellProduct(shop, product, 3);
        });
    }

    @Test
    void testSellProduct_ThrowsExpiredProductException() {
        FoodProduct expiredProduct = mock(FoodProduct.class);
        when(shop.getStock()).thenReturn(new HashMap<>(Map.of((Product) expiredProduct, 5)));
        when(productService.isExpired(expiredProduct)).thenReturn(true);
        when(expiredProduct.getName()).thenReturn("Expired Cheese");

        assertThrows(ExpiredProductException.class, () -> {
            shopService.sellProduct(shop, (Product) expiredProduct, 2);
        });
    }

    // --- Test calculateProfit() ---
    @Test
    void testCalculateProfit() {
        when(shop.getTotalIncome()).thenReturn(new BigDecimal("1000"));
        when(shop.getTotalDeliveryCosts()).thenReturn(new BigDecimal("300"));
        when(shop.getCashiers()).thenReturn(Set.of(cashier));

        when(cashier.getSalary()).thenReturn(new BigDecimal("400"));

        assertEquals(new BigDecimal("300"), shopService.calculateProfit(shop)); // 1000 - (300 + 400)
    }

    // --- Test calculateExpensesForSalaries() ---
    @Test
    void testCalculateExpensesForSalaries() {
        Cashier cashier1 = mock(Cashier.class);
        Cashier cashier2 = mock(Cashier.class);
        when(shop.getCashiers()).thenReturn(Set.of(cashier1, cashier2));

        when(cashier1.getSalary()).thenReturn(new BigDecimal("500"));
        when(cashier2.getSalary()).thenReturn(new BigDecimal("700"));

        assertEquals(new BigDecimal("1200"), shopService.calculateExpensesForSalaries(shop));
    }

    // --- Test addCashier() and getCashiers() ---
    @Test
    void testAddCashier() {
        shopService.addCashier(shop, cashier);

        assertTrue(shop.getCashiers().contains(cashier));
    }

    @Test
    void testAddCashier_WhenCashierAlreadyHired_ShouldThrowException() {
        shopService.addCashier(shop, cashier); // First time - should succeed

        // Second time - should throw an exception
        assertThrows(CashierAlreadyHiredException.class, () -> shopService.addCashier(shop, cashier));
    }

    @Test
    void testGetCashiers_EmptyList() {
        when(shop.getCashiers()).thenReturn(new HashSet<>());

        Set<Cashier> cashiers = shopService.getCashiers(shop);

        assertNotNull(cashiers);
        assertTrue(cashiers.isEmpty());
    }

    @Test
    void testGetCashiers_WithCashiers() {
        Cashier cashier1 = mock(Cashier.class);
        Cashier cashier2 = mock(Cashier.class);
        Set<Cashier> cashierList = Set.of(cashier1, cashier2);

        when(shop.getCashiers()).thenReturn(cashierList);

        Set<Cashier> retrievedCashiers = shopService.getCashiers(shop);

        assertEquals(2, retrievedCashiers.size());
        assertTrue(retrievedCashiers.contains(cashier1));
        assertTrue(retrievedCashiers.contains(cashier2));
    }

}

