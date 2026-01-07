package com.parking_reservation_system.dto.request;

import com.parking_reservation_system.model.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record VehicleDto(

        int id,

        @NotBlank(message = "Plate number is required")
        @Size(max = 10, message = "Plate number cannot exceed 10 characters")
        String plateNumber,

        @Positive(message = "Model year must be a positive number")
        int modelYear,

        @NotBlank(message = "Model name is required")
        String modelName,

        @Positive(message = "Vehicle width must be positive")
        double vehicleWidth,

        @Positive(message = "Vehicle depth must be positive")
        double vehicleDepth,

        @NotNull(message = "Vehicle type must be specified")
        @Enumerated(EnumType.STRING)
        VehicleType type,

        @NotNull(message = "a vehicle should be tied to user")
        Integer user_id

) {}
