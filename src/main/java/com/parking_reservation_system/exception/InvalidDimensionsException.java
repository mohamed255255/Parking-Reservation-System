package com.parking_reservation_system.exception;

public class InvalidDimensionsException extends IllegalArgumentException {

    public InvalidDimensionsException(String message) {
        super(message);
    }

    public InvalidDimensionsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
