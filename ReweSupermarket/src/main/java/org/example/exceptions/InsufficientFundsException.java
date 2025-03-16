package org.example.exceptions;

public class InsufficientFundsException extends IllegalArgumentException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
