package org.example.exceptions;

public class EmptyCustomersQueue extends NullPointerException {
    public EmptyCustomersQueue(String message) {
        super(message);
    }
}
