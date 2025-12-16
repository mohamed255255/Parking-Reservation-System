package com.garage_system.validator;
import com.garage_system.DTO.request.ResetPasswordDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchPasswordsValidator implements ConstraintValidator<MatchPasswordsValidation, ResetPasswordDto> {
    @Override
    public boolean isValid(ResetPasswordDto dto , ConstraintValidatorContext context) {
        return dto.getNewPassword().equals(dto.getConfirmedNewPassword());
    }
}