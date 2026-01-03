package com.parking_reservation_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerificationDto(

        @NotBlank(message = "please insert the verification code")
        String verificationCode,

        @Email(message = "email format is not valid")
        String email

) {}
