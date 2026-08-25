package com.parking_reservation_system.exception;

public class TokenMismatchException extends RuntimeException {

    public TokenMismatchException(String message) {
        super(message);
    }

    public TokenMismatchException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
