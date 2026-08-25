package com.parking_reservation_system.exception;

public class QRCodeGenerationException extends RuntimeException {

    public QRCodeGenerationException(String message) {
        super(message);
    }

    public QRCodeGenerationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
