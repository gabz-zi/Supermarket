package org.example.models;

import org.example.services.ReceiptService;

import java.util.Queue;
import java.util.LinkedList;
import java.util.UUID;

public class CashDesk {
    private final String id;
    private final Queue<Customer> queue;

    public CashDesk() {
        this.id = UUID.randomUUID().toString();
        this.queue = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public Queue<Customer> getQueue() {
        return queue;
    }
}
