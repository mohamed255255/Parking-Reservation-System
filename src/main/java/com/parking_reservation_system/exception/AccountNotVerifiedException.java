package com.parking_reservation_system.exception;

final public class AccountNotVerifiedException extends RuntimeException {

    public AccountNotVerifiedException(String message) {
        super(message);
    }

    public AccountNotVerifiedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
