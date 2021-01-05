package com.example.shoppinglistapi.service.tools.normalizer;

public class NormalizationException extends Exception {

    public NormalizationException() {
        super();
    }

    public NormalizationException(String message) { super(message); }

    public NormalizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NormalizationException(Throwable cause) {
        super(cause);
    }
}
