package com.garage_system.mapper;

import com.garage_system.DTO.request.GarageDto;
import com.garage_system.Model.Garage;

public class GarageMapper {

    // Convert GarageDto -> Garage entity
    public static Garage toEntity(GarageDto dto) {
        if (dto == null) return null;

        Garage garage = new Garage();
        garage.setName(dto.getName());
        garage.setLocation(dto.getLocation());
        garage.setCapactiy(dto.getCapactiy());
        garage.setActive(dto.isActive());

        return garage;
    }

    public static GarageDto toDto(Garage garage) {
        if (garage == null) return null;

        GarageDto dto = new GarageDto();
        dto.setId(garage.getId());
        dto.setName(garage.getName());
        dto.setLocation(garage.getLocation());
        dto.setCapactiy(garage.getCapactiy());
        dto.setActive(garage.isActive());
        return dto;
    }
}
