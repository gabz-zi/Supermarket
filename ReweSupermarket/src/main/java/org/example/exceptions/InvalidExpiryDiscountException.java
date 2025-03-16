package org.example.exceptions;

public class InvalidExpiryDiscountException extends IllegalArgumentException {
    public InvalidExpiryDiscountException(String message) {
        super(message);
    }
}
