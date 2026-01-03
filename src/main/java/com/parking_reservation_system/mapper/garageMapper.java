package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.GarageDto;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.dto.response.GarageResponseDto;

public class GarageMapper {

    public static Garage toEntity(GarageDto dto) {
        if (dto == null) return null;

        Garage garage = new Garage();
        garage.setName(dto.name());       
        garage.setLocation(dto.location());
        garage.setCapactiy(dto.capacity());
        garage.setActive(dto.isActive());

        return garage;
    }

    public static GarageDto toDto(Garage garage) {
        if (garage == null) return null;

        return new GarageDto(
                garage.getId(),
                garage.getName(),
                garage.getLocation(),
                garage.isActive(),
                garage.getCapactiy()
        );
    }

    public static GarageResponseDto toResponseDto(Garage garage) {
        if (garage == null) return null;

        return new GarageResponseDto(
                garage.getId(),
                garage.getName(),
                garage.getLocation(),
                garage.isActive(),
                garage.getCapactiy()
        );
    }
}
