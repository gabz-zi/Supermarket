package org.example.exceptions;

public class CustomerInsufficientFundsException extends IllegalStateException {
    public CustomerInsufficientFundsException(String message) {
        super(message);
    }
}
