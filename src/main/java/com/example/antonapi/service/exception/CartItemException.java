package com.example.antonapi.service.exception;

public class CartItemException extends Exception {

    public CartItemException() {
        super();
    }

    public CartItemException(String message) {
        super(message);
    }

    public CartItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartItemException(Throwable cause) {
        super(cause);
    }
}
