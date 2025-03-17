package org.example.exceptions;

public class CustomerIsJumpingTheQueueException extends IllegalStateException {
    public CustomerIsJumpingTheQueueException(String message) {
        super(message);
    }
}
