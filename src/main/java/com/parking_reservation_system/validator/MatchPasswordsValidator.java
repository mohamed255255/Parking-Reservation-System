package com.parking_reservation_system.validator;

import com.parking_reservation_system.dto.request.ResetPasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchPasswordsValidator
        implements ConstraintValidator<MatchPasswordsValidation, ResetPasswordRequest> {
    @Override
    public boolean isValid(ResetPasswordRequest dto, ConstraintValidatorContext context) {
        return dto.newPassword().equals(dto.confirmedNewPassword());
    }
}
