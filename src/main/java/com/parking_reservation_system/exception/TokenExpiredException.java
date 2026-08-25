package com.parking_reservation_system.exception;

public class TokenExpiredException extends RuntimeException {
    
    public TokenExpiredException(String message){
        super(message);
    }

    public TokenExpiredException(String message , Throwable throwable){
        super(message , throwable);
    } 
    
}
