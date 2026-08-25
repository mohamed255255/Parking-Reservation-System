package com.parking_reservation_system.dto.request;

import com.parking_reservation_system.validator.MatchPasswordsValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@MatchPasswordsValidation
public record ResetPasswordRequest(
        @NotBlank(message = "Verification code is required") String resetPasswordcode,
        @Email @NotBlank(message = "email field is missing") String email,
        @Size(min = 6, max = 20, message = "passwords should be greater than 6 and less than 20")
                @NotBlank(message = "Old password field is missing")
                @NotBlank(message = "New password field is missing")
                String newPassword,
        @NotBlank(message = "Confirm password field is missing") String confirmedNewPassword) {}
