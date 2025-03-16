package org.example.exceptions;

public class SellingExpiredProductException extends IllegalStateException {
    public SellingExpiredProductException(String message) {
        super(message);
    }
}
