package org.example.exceptions;

public class InvalidCustomerNameException extends IllegalArgumentException {
    public InvalidCustomerNameException(String message) {
        super(message);
    }
}
