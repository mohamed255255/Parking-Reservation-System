package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.dto.response.VehicleResponseDto;
import com.parking_reservation_system.model.Vehicle;

public class VehicleMapper {

    public static Vehicle toEntity(VehicleDto dto) {
        if (dto == null) return null;

        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(dto.plateNumber());
        vehicle.setModelYear(dto.modelYear());
        vehicle.setModelName(dto.modelName());
        vehicle.setVehicleWidth(dto.vehicleWidth());
        vehicle.setVehicleDepth(dto.vehicleDepth());
        vehicle.setType(dto.type());
        return vehicle;
    }

    public static VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) return null;

        return new VehicleDto(
                vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getModelYear(),
                vehicle.getModelName(),
                vehicle.getVehicleWidth(),
                vehicle.getVehicleDepth(),
                vehicle.getType(),
                vehicle.getUser() != null ? vehicle.getUser().getId() : 0);
    }

    public static VehicleResponseDto toResponseDto(Vehicle vehicle) {
        if (vehicle == null) return null;

        return new VehicleResponseDto(
                vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getModelYear(),
                vehicle.getModelName(),
                vehicle.getVehicleWidth(),
                vehicle.getVehicleDepth(),
                vehicle.getType(),
                vehicle.getUser() != null ? vehicle.getUser().getId() : 0);
    }
}
