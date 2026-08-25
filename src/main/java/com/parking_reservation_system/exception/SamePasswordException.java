package com.parking_reservation_system.exception;

public class SamePasswordException extends RuntimeException {

    public SamePasswordException(String message) {
        super(message);
    }

    public SamePasswordException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
