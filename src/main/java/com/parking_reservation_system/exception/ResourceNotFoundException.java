package com.parking_reservation_system.exception;

public class ResourceNotFoundException  extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);
    }
    
}
