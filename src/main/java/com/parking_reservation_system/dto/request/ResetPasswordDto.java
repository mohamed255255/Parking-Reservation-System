package com.parking_reservation_system.dto.request;

import com.parking_reservation_system.validator.MatchPasswordsValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@MatchPasswordsValidation
public record ResetPasswordDto(

        @Email
        @NotBlank(message = "email field is missing")
        String email,

        @Size(min = 6, max = 20, message = "passwords should be greater than 6 and less than 20")
        @NotBlank(message = "password field is missing")
        String newPassword,

        String confirmedNewPassword

) {}
