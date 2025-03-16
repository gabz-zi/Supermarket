package org.example.exceptions;

public class ExpiredProductException extends IllegalStateException {
    public ExpiredProductException(String message) {
        super(message);
    }
}
