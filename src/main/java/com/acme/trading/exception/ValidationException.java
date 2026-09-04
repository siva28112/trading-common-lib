package com.acme.trading.exception;

/**
 * Thrown when order or request validation fails.
 */
public class ValidationException extends TradingException {

    public ValidationException(String message) {
        super(message);
    }
}
