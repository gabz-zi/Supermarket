package org.example.exceptions;

public class InvalidShopNameException extends IllegalArgumentException {
    public InvalidShopNameException(String message) {
        super(message);
    }
}
