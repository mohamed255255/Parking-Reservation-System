package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.GarageRequest;
import com.parking_reservation_system.dto.response.GarageResponse;
import com.parking_reservation_system.model.Garage;

public final class GarageMapper {

    private GarageMapper() { 
    }

    public static Garage toEntity(GarageRequest dto) {
        if (dto == null) {
            throw new IllegalArgumentException("GarageRequest must not be null");
        }

        Garage garage = new Garage();

        garage.setName(dto.name());
        garage.setLocation(dto.location());
        garage.setCapactiy(dto.capacity());
        garage.setActive(dto.isActive());
        return garage;
    }

    public static GarageRequest toDto(Garage garage) {
        if (garage == null) {
            throw new IllegalArgumentException("Garage entity must not be null");
        }

        return new GarageRequest(
                garage.getId(),
                garage.getName(),
                garage.getLocation(),
                garage.isActive(),
                garage.getCapactiy()
        );
    }

    public static GarageResponse toResponseDto(Garage garage) {
        if (garage == null) {
            throw new IllegalArgumentException("Garage entity must not be null");
        }

        return new GarageResponse(
                garage.getId(),
                garage.getName(),
                garage.getLocation(),
                garage.isActive(),
                garage.getCapactiy()
        );
    }
}