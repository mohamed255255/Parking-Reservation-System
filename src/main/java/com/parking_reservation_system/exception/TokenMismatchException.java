package com.parking_reservation_system.exception;

final public class TokenMismatchException extends RuntimeException {

    public TokenMismatchException(String message) {
        super(message);
    }

    public TokenMismatchException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
