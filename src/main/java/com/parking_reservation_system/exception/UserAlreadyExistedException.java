package com.parking_reservation_system.exception;

final public class UserAlreadyExistedException extends RuntimeException {

    public UserAlreadyExistedException(String message) {
        super(message);
    }

    public UserAlreadyExistedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
