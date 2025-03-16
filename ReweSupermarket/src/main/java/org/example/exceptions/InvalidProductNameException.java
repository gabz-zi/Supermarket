package org.example.exceptions;

public class InvalidProductNameException extends IllegalArgumentException {
    public InvalidProductNameException(String message) {
        super(message);
    }
}
