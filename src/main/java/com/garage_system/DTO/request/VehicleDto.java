package com.garage_system.DTO.request;

import com.garage_system.Model.VehicleType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VehicleDto {

    private Integer id;

    @NotBlank(message = "Plate number is required")
    @Size(max = 10, message = "Plate number cannot exceed 10 characters")
    private String plateNumber;

    @Positive(message = "Model year must be a positive number")
    private int modelYear;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @Positive(message = "Vehicle width must be positive")
    private double vehicleWidth;

    @Positive(message = "Vehicle depth must be positive")
    private double vehicleDepth;

    @NotNull(message = "Vehicle type must be specified")
    @Enumerated(EnumType.STRING)
    private VehicleType type;

}


