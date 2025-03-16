package org.example.exceptions;

public class InvalidCashierSalaryException extends IllegalArgumentException {
    public InvalidCashierSalaryException(String message) {
        super(message);
    }
}
