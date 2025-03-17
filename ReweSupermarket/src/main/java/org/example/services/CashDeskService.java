package org.example.services;

import org.example.exceptions.CustomerIsJumpingTheQueueException;
import org.example.exceptions.CustomerNotInQueueException;
import org.example.exceptions.EmptyCustomersQueue;
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
        if (!cashDesk.getQueue().isEmpty()) {
            return cashDesk.getQueue().poll();
        }
        throw new EmptyCustomersQueue("There are no more customers in the queue");
    }

    public void checkout(CashDesk cashDesk, Customer customer) {
        if (!cashDesk.getQueue().contains(customer)) {
            throw new CustomerNotInQueueException("Customer is not in the queue");
        }
        if (!cashDesk.getQueue().isEmpty() && !cashDesk.getQueue().peek().equals(customer)) {
            throw new CustomerIsJumpingTheQueueException("Customer must wait for their turn.");
        }
        cashDesk.getQueue().remove(customer);
    }
}
