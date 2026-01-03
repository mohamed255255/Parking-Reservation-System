package com.parking_reservation_system.dto.response;

public record EmailVerificationResponseDto(
        String verificationCode,
        String email

) {}
