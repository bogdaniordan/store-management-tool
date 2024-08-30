package com.store_management.exception;

public class InventoryAlreadyExists extends RuntimeException {
    public InventoryAlreadyExists(String message) {
        super(message);
    }
}
