package org.example.exceptions;

public class InvalidFoodProductExpireDateException extends IllegalArgumentException {
    public InvalidFoodProductExpireDateException(String message) {
        super(message);
    }
}
