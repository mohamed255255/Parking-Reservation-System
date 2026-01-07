package com.parking_reservation_system.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserDto(

        Integer id,

        @Email(message = "Email format is invalid")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "password cant be filled with whitespace / null / empty")
        @Size(min = 6, max = 20, message = "passwords should be greater than 6 and less than 20")
        String password
) {}
