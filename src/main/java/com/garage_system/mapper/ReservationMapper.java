package com.garage_system.mapper;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Model.Reservation;

public class ReservationMapper {
   
    public static Reservation toEntity(ReservationDto dto) {
        if (dto == null) return null;

        Reservation reservation = new Reservation();
        reservation.setStartingTime(dto.getStartingTime());
        reservation.setEndingTime(dto.getEndigTime());
        reservation.setStatus(Reservation.Status.PENDING);
        return reservation;
    }

    public static ReservationDto toDto(Reservation entity) {
        if (entity == null) return null;
        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId());
        dto.setStartingTime(entity.getStartingTime());
        dto.setEndigTime(entity.getEndingTime());
        dto.setUser_id(entity.getUser().getId());
        dto.setSlot_id(entity.getSlot().getId());
        dto.setGarage_id(entity.getGarage().getId());  
        return dto;
    }
}
