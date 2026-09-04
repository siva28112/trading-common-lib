package com.acme.trading.exception;

/**
 * Base runtime exception for trading domain failures.
 */
public class TradingException extends RuntimeException {

    public TradingException(String message) {
        super(message);
    }

    public TradingException(String message, Throwable cause) {
        super(message, cause);
    }
}
