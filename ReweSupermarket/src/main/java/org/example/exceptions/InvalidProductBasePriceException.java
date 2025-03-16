package org.example.exceptions;

public class InvalidProductBasePriceException extends IllegalArgumentException {
    public InvalidProductBasePriceException(String message) {
        super(message);
    }
}
