package org.example.exceptions;

public class OutOfStockException extends IllegalArgumentException {
    public OutOfStockException(String message) {
        super(message);
    }
}
