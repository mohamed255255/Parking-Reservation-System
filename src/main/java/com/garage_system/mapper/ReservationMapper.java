package com.garage_system.mapper;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Model.Reservation;

public class ReservationMapper {
   
    public static Reservation toEntity(ReservationDto dto) {
        if (dto == null) return null;

        Reservation reservation = new Reservation();
        reservation.setStartingTime(dto.getStartingTime());

        return reservation;
    }

    public static ReservationDto toDto(Reservation entity) {
        if (entity == null) return null;

        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId());
        dto.setStartingTime(entity.getStartingTime());

        return dto;
    }
}
