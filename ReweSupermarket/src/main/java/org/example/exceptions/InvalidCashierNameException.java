package org.example.exceptions;

public class InvalidCashierNameException extends IllegalArgumentException {
    public InvalidCashierNameException(String message) {
        super(message);
    }
}
