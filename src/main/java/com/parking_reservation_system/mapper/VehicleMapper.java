package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.VehicleRequest;
import com.parking_reservation_system.dto.response.VehicleResponse;
import com.parking_reservation_system.model.Vehicle;

public final class VehicleMapper {

    private VehicleMapper() {}

    public static Vehicle toEntity(VehicleRequest dto) {
        if (dto == null) {
            throw new IllegalArgumentException("VehicleRequest must not be null");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(dto.plateNumber());
        vehicle.setModelYear(dto.modelYear());
        vehicle.setModelName(dto.modelName());
        vehicle.setVehicleWidth(dto.vehicleWidth());
        vehicle.setVehicleDepth(dto.vehicleDepth());
        vehicle.setType(dto.type());

        return vehicle;
    }

    public static VehicleRequest toDto(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle entity must not be null");
        }

        Integer userId = (vehicle.getUser() != null) ? vehicle.getUser().getId() : null;

        return new VehicleRequest(
                vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getModelYear(),
                vehicle.getModelName(),
                vehicle.getVehicleWidth(),
                vehicle.getVehicleDepth(),
                vehicle.getType(),
                userId
        );
    }

    public static VehicleResponse toResponseDto(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle entity must not be null");
        }

        Integer userId = (vehicle.getUser() != null) ? vehicle.getUser().getId() : null;

        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getModelYear(),
                vehicle.getModelName(),
                vehicle.getVehicleWidth(),
                vehicle.getVehicleDepth(),
                vehicle.getType(),
                userId
        );
    }
}