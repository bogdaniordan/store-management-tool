package com.store_management.exception;

public class UserDoesNotExist extends RuntimeException {

    public UserDoesNotExist(String message) {
        super(message);
    }
}
