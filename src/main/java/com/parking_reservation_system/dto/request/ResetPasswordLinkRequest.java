package com.parking_reservation_system.dto.request;

import jakarta.validation.constraints.Email;

public record ResetPasswordLinkRequest(@Email(message = "email format is wrong") String email) {}
