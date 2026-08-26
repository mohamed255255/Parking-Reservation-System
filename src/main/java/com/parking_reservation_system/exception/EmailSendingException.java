package com.parking_reservation_system.exception;

final public class EmailSendingException extends RuntimeException {

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
