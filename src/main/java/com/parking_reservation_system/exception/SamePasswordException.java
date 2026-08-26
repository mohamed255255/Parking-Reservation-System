package com.parking_reservation_system.exception;

final public class SamePasswordException extends RuntimeException {

    public SamePasswordException(String message) {
        super(message);
    }

    public SamePasswordException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
