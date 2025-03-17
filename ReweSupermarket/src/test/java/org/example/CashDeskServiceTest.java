package org.example;

import org.example.exceptions.CustomerIsJumpingTheQueueException;
import org.example.exceptions.CustomerNotInQueueException;
import org.example.exceptions.EmptyCustomersQueue;
import org.example.models.CashDesk;
import org.example.models.Customer;
import org.example.services.CashDeskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CashDeskServiceTest {

    private CashDeskService cashDeskService;
    private CashDesk cashDesk;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setUp() {
        cashDeskService = new CashDeskService();
        cashDesk = mock(CashDesk.class);
        customer1 = mock(Customer.class);
        customer2 = mock(Customer.class);

        // Setup queue behavior
        Queue<Customer> queue = new LinkedList<>();
        when(cashDesk.getQueue()).thenReturn(queue);
    }

    @Test
    public void testAddCustomerToQueue() {
        cashDeskService.addCustomerToQueue(customer1, cashDesk);

        assertFalse(cashDesk.getQueue().isEmpty());
        assertEquals(customer1, cashDesk.getQueue().peek());
    }

    @Test
    public void testNextCustomer_WhenQueueNotEmpty() {
        cashDeskService.addCustomerToQueue(customer1, cashDesk);
        cashDeskService.addCustomerToQueue(customer2, cashDesk);

        Customer next = cashDeskService.nextCustomer(cashDesk);
        assertEquals(customer1, next);
        assertFalse(cashDesk.getQueue().contains(customer1));
    }

    @Test
    public void testNextCustomer_WhenQueueEmpty_ShouldThrowException() {
        assertThrows(EmptyCustomersQueue.class, () -> cashDeskService.nextCustomer(cashDesk));
    }

    @Test
    public void testCheckout_WhenCustomerInQueue() {
        cashDeskService.addCustomerToQueue(customer1, cashDesk);

        cashDeskService.checkout(cashDesk, customer1);

        assertFalse(cashDesk.getQueue().contains(customer1));
    }

    @Test
    public void testCheckout_WhenCustomerNotInQueue_ShouldThrowException() {
        cashDeskService.addCustomerToQueue(customer1, cashDesk);

        assertThrows(CustomerNotInQueueException.class, () -> cashDeskService.checkout(cashDesk, customer2));
    }

    @Test
    public void testCheckout_WhenCustomerIsJumpingTheQueue_ShouldThrowException() {
        cashDeskService.addCustomerToQueue(customer1, cashDesk);
        cashDeskService.addCustomerToQueue(customer2, cashDesk);

        assertThrows(CustomerIsJumpingTheQueueException.class, () -> cashDeskService.checkout(cashDesk, customer2));
    }
}

