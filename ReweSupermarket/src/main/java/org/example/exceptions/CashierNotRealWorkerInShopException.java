package org.example.exceptions;

public class CashierNotRealWorkerInShopException extends IllegalStateException {
    public CashierNotRealWorkerInShopException(String message) {
        super(message);
    }
}
