package org.example.exceptions;

public class InvalidDaysBeforeExpiryDiscountException extends IllegalArgumentException {
    public InvalidDaysBeforeExpiryDiscountException(String message) {
        super(message);
    }
}
