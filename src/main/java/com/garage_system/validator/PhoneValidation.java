package com.garage_system.validator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Target(ElementType.FIELD) /// we will validate a field in the DTO
@Retention(RetentionPolicy.RUNTIME) /// work by api when server runs at run time
@Constraint(validatedBy = PhoneValidator.class) /// use logic of phoneValidator class

public @interface PhoneValidation {
      String message() default  "Invalid phone number" ;
      /// need to be put for compialtion even if i wont use them
      Class<?>[] groups() default {};  
      Class<? extends Payload>[] payload() default {};

}
