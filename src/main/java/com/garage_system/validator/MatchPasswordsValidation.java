package com.garage_system.validator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = MatchPasswordsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchPasswordsValidation {

    String message() default "New password and confirmation password must match.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}