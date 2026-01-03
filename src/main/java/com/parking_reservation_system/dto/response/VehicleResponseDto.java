package com.parking_reservation_system.dto.response;

import com.parking_reservation_system.model.VehicleType;

public record VehicleResponseDto(
        int id,
        String plateNumber,
        int modelYear,
        String modelName,
        double vehicleWidth,
        double vehicleDepth,
        VehicleType type,
        int userId
) {}
