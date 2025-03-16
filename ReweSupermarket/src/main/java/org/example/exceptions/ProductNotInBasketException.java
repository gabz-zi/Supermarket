package org.example.exceptions;

public class ProductNotInBasketException extends IllegalStateException {
    public ProductNotInBasketException(String message) {
        super(message);
    }
}
