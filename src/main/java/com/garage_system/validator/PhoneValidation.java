package com.garage_system.validator;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneValidation extends  ResponseEntityExceptionHandler{
    
}
