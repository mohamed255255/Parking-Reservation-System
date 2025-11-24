package com.garage_system.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

       /// handle any field(arguemant) error 
       @ExceptionHandler(MethodArgumentNotValidException.class)
       @ResponseStatus(HttpStatus.BAD_REQUEST) // cuz the default is 200 even if there is an exception  
       public Map<String , String> handleMethodArgumentNotValidException( MethodArgumentNotValidException e){
              
               Map<String , String> errorMap = new HashMap<>() ;
               e.getBindingResult().getAllErrors().forEach(error -> {
                       String fieldName = ((FieldError) error).getField();
                       String message   = error.getDefaultMessage(); /// get the message we wrote in annotation
                       errorMap.put(fieldName , message) ;
               });
               return errorMap ;
       }


        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<?> handleWrongCredentials(BadCredentialsException ex) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("wrong email or password");
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Object> handleNotFoundEntities(ResourceNotFoundException ex) {
                Map<String, Object> body = new HashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                body.put("error", "Internal Server Error");
                body.put("message", ex.getMessage());
                return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }




}
