package org.example.exceptions;

public class InvalidCustomerBudgetException extends IllegalArgumentException {
    public InvalidCustomerBudgetException(String message) {
        super(message);
    }
}
