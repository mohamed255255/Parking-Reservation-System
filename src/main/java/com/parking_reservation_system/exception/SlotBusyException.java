package com.parking_reservation_system.exception;

final public class SlotBusyException extends RuntimeException {

    public SlotBusyException(String message) {
        super(message);
    }

    public SlotBusyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
