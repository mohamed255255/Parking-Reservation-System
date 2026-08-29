package com.parking_reservation_system.dto.request;

import com.parking_reservation_system.model.VehicleType;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VehicleRequest(
        @NotBlank(message = "Plate number is required")
                @Size(max = 4, message = "Plate number cannot exceed 4 characters")
                String plateNumber,
        @Positive(message = "Model year must be a positive number") @Nullable int modelYear,
        @NotBlank(message = "Model name is required") String modelName,
        @Positive(message = "Vehicle width must be positive")
                @NotNull(message = "Vehicle width is required")
                Integer vehicleWidth,
        @Positive(message = "Vehicle depth must be positive")
                @NotNull(message = "Vehicle depth is required")
                Integer vehicleDepth,
        @NotNull(message = "Vehicle type must be specified") @Enumerated(EnumType.STRING)
                VehicleType type,
        @NotNull(message = "a vehicle should be tied to user") Integer userId) {}
