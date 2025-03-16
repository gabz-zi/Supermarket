package org.example.exceptions;

public class CustomerNotInQueueException extends IllegalStateException {
    public CustomerNotInQueueException(String message) {
        super(message);
    }
}
