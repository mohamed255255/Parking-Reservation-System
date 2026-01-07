package com.parking_reservation_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GarageDto(

        Integer id,

        @NotBlank(message = "name can not be empty")
        String name,

        @NotNull(message = "location field is required")
        String location,

        boolean isActive,

        @Positive(message = "capacity of the garage should be positive value")
        Integer capacity
) {}
