package org.example.services;

import org.example.models.CashDesk;
import org.example.models.Cashier;
import org.example.models.Customer;
import org.example.models.Receipt;

import java.util.LinkedList;
import java.util.Queue;

public class CashDeskService {

    public void addCustomerToQueue(Customer customer, CashDesk cashDesk) {
        cashDesk.getQueue().offer(customer);
    }

    public Customer nextCustomer(CashDesk cashDesk) {
        return cashDesk.getQueue().poll();
    }

    public void checkout(CashDesk cashDesk, Customer customer) {
        if (!cashDesk.getQueue().contains(customer)) {
            throw new IllegalStateException("Customer is not in the queue");
        }
        cashDesk.getQueue().remove(customer);
    }
}
