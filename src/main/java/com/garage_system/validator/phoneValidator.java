package com.garage_system.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

class PhoneValidator implements ConstraintValidator<PhoneValidation, String> {

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^(?:\\+20|0)?1[0-2,5]\\d{8}$");

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }

        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

}
