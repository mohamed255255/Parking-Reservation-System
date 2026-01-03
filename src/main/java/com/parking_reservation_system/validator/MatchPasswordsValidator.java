package com.parking_reservation_system.validator;
import com.parking_reservation_system.dto.request.ResetPasswordDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchPasswordsValidator implements ConstraintValidator<MatchPasswordsValidation, ResetPasswordDto> {
    @Override
    public boolean isValid(ResetPasswordDto dto , ConstraintValidatorContext context) {
        return dto.newPassword().equals(dto.confirmedNewPassword());
    }
}