package org.example.exceptions;

public class CashierAlreadyHiredException extends IllegalStateException {
    public CashierAlreadyHiredException(String message) {
        super(message);
    }
}
