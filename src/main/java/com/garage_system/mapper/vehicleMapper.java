package com.garage_system.mapper;

import com.garage_system.dto.request.VehicleDto;
import com.garage_system.model.Vehicle;

public class VehicleMapper {

    public static Vehicle toEntity(VehicleDto dto) {
        if (dto == null) return null;

        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setModelYear(dto.getModelYear());
        vehicle.setModelName(dto.getModelName());
        vehicle.setVehicleWidth(dto.getVehicleWidth());
        vehicle.setVehicleDepth(dto.getVehicleDepth());
        vehicle.setType(dto.getType());

        return vehicle;
    }

    public static VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) return null;

        VehicleDto dto = new VehicleDto();
        dto.setPlateNumber(vehicle.getPlateNumber());
        dto.setModelYear(vehicle.getModelYear());
        dto.setModelName(vehicle.getModelName());
        dto.setVehicleWidth(vehicle.getVehicleWidth());
        dto.setVehicleDepth(vehicle.getVehicleDepth());
        dto.setType(vehicle.getType());

        return dto;
    }
}
