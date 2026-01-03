package com.parking_reservation_system.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
        // global error vs field error
        // handle any field(arguemant) error 
        // global error like @MatchPasswordsValidation at the class level
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
                Map<String, String> errorMap = new HashMap<>();

                e.getBindingResult().getAllErrors().forEach(error -> {

                    String fieldName;
                    String message = error.getDefaultMessage();
                
                    if (error instanceof FieldError)
                       fieldName = ((FieldError) error).getField();
                    else if (error instanceof ObjectError) 
                       fieldName = error.getObjectName();    
                    else 
                       fieldName = "unknown";

                    errorMap.put(fieldName, message);
        });
        return errorMap;
     }
       // wrong credentialss
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<?> handleWrongCredentials(BadCredentialsException ex) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("wrong email or password");
        }
        /// null resouces from DB
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Object> handleNotFoundEntities(ResourceNotFoundException ex) {
                Map<String, Object> body = new HashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                body.put("error", "Internal Server Error");
                body.put("message", ex.getMessage());
                return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        /// data integrity violation
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<?> handleDbConstraintViolations(DataIntegrityViolationException ex){
                Map<String, Object> body = new HashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.BAD_REQUEST);
                body.put("error", "constraint violation while you try to CRUD");
                body.put("message", ex.getMessage());
           return new ResponseEntity<>(body , HttpStatus.BAD_REQUEST);
        }

  /// Runtime exceptions 
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<?> handleRTE(RuntimeException ex){
                Map<String, Object> body = new HashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
                body.put("error", "Runtime error");
                body.put("message", ex.getMessage());
           return new ResponseEntity<>(body , HttpStatus.INTERNAL_SERVER_ERROR);
        }



}
