package com.parking_reservation_system.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parking_reservation_system.model.Role;
import com.parking_reservation_system.validator.PhoneValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(

        Integer id,

        @NotBlank(message = "empty names are not allowed")
        String name,

        @Email(message = "Email format is invalid")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "password cant be filled with whitespace / null / empty")
        @Size(min = 6, max = 20, message = "passwords should be greater than 6 and less than 20")
        String password,

        @PhoneValidation
        String phone,

        @NotNull(message = "a role must be assigned")
        List<Role> roles

) {}
