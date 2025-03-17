package org.example;

import org.example.exceptions.CashierNotRealWorkerInShopException;
import org.example.exceptions.CustomerNotInQueueException;
import org.example.exceptions.InsufficientFundsException;
import org.example.models.*;
import org.example.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CashierServiceTest {

    private CashierService cashierService;
    private ReceiptService receiptService;
    private ShopService shopService;
    private ProductService productService;
    private CustomerService customerService;
    private CashDeskService cashDeskService;
    private Shop shop;
    private Cashier cashier;
    private Customer customer;
    private CashDesk cashDesk;
    private Receipt receipt;
    private Product product;
    private Queue<Customer> queue;  // Mocked Queue

    @BeforeEach
    public void setUp() {
        // Mock dependencies
        receiptService = mock(ReceiptService.class);
        shopService = mock(ShopService.class);
        productService = mock(ProductService.class);
        customerService = mock(CustomerService.class);
        cashDeskService = mock(CashDeskService.class);

        // Create service instance
        cashierService = new CashierService(receiptService, shopService, productService, customerService, cashDeskService);

        // Mock models
        shop = mock(Shop.class);
        cashier = mock(Cashier.class);
        customer = mock(Customer.class);
        cashDesk = mock(CashDesk.class);
        receipt = mock(Receipt.class);
        product = mock(Product.class);

        // Setup cashier assigned desk
        when(cashier.getAssignedDesk()).thenReturn(cashDesk);

        // Mock the Queue for the cash desk
        queue = mock(Queue.class);
        when(cashDesk.getQueue()).thenReturn(queue);
    }

    @Test
    public void testProcessPurchase_SuccessfulPurchase() {
        // Create mocks for the required objects
        Customer customer = mock(Customer.class);
        Cashier cashier = mock(Cashier.class);
        Shop shop = mock(Shop.class);
        Receipt receipt = mock(Receipt.class);
        CashDesk cashDesk = mock(CashDesk.class);  // Mock the cash desk
        Product product = mock(Product.class);  // Mock the product
        Queue<Customer> queue = mock(Queue.class);  // Mock the queue

        // Use a real basket (Fix)
        Map<Product, Integer> basket = new HashMap<>();
        basket.put(product, 2);

        // Fix: Ensure cashier returns a non-null assigned cash desk
        when(cashier.getAssignedDesk()).thenReturn(cashDesk);

        // Fix: Ensure customer has a real basket
        when(customer.getBasket()).thenReturn(basket);

        // Setup shopService to return the cashier
        when(shopService.getCashiers(shop)).thenReturn(Collections.singleton(cashier));;

        // Fix: Ensure cashDesk.getQueue() returns a non-null queue
        when(cashDesk.getQueue()).thenReturn(queue);
        when(queue.contains(customer)).thenReturn(true);
        when(queue.poll()).thenReturn(customer);  // Simulate processing the customer

        // Setup receiptService to return a total cost
        BigDecimal totalCost = BigDecimal.valueOf(50);
        when(receiptService.calculateTotal(shop, basket)).thenReturn(totalCost);

        // Customer can afford the total cost
        when(customerService.canAfford(customer, totalCost)).thenReturn(true);

        // Mock the receipt creation
        when(receiptService.createReceipt(shop, customer, cashier)).thenReturn(receipt);

        // Execute the processPurchase method
        Receipt issuedReceipt = cashierService.processPurchase(customer, cashier, shop);

        // Verify that the basket was processed and the sellProduct method was called
        verify(shopService).sellProduct(shop, product, 2);  // This should now be called

        // Verify that other methods were called
        verify(customerService).deduct(customer, totalCost);
        verify(cashDeskService).checkout(cashDesk, customer);
        verify(receiptService).createReceipt(shop, customer, cashier);

        // Ensure the basket was cleared after purchase
        assertTrue(basket.isEmpty());

        // Verify that the correct receipt was returned
        assertEquals(receipt, issuedReceipt);
    }



    @Test
    public void testProcessPurchase_CustomerNotInQueue_ThrowsException() {
        when(shopService.getCashiers(shop)).thenReturn(Collections.singleton(cashier));;
        when(queue.contains(customer)).thenReturn(false);  // Simulate customer not in the queue

        assertThrows(CustomerNotInQueueException.class, () -> {
            cashierService.processPurchase(customer, cashier, shop);
        });
    }

    @Test
    public void testProcessPurchase_CashierNotInShop_ThrowsException() {
        when(shopService.getCashiers(shop)).thenReturn(Collections.emptySet());
        when(queue.contains(customer)).thenReturn(true);

        assertThrows(CashierNotRealWorkerInShopException.class, () -> {
            cashierService.processPurchase(customer, cashier, shop);
        });
    }

    @Test
    public void testProcessPurchase_CustomerCannotAfford_ThrowsException() {
        when(shopService.getCashiers(shop)).thenReturn(Collections.singleton(cashier));
        when(queue.contains(customer)).thenReturn(true);

        Map<Product, Integer> basket = new HashMap<>();
        basket.put(product, 2);
        when(customer.getBasket()).thenReturn(basket);

        BigDecimal totalCost = BigDecimal.valueOf(50);
        when(receiptService.calculateTotal(shop, basket)).thenReturn(totalCost);

        when(customerService.canAfford(customer, totalCost)).thenReturn(false);

        assertThrows(InsufficientFundsException.class, () -> {
            cashierService.processPurchase(customer, cashier, shop);
        });

        verify(customerService, never()).deduct(any(), any());
        verify(shopService, never()).sellProduct(any(), any(), anyInt());
        verify(receiptService, never()).createReceipt(any(), any(), any());
    }
}
