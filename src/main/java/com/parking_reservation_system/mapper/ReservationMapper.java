package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.ReservationDto;
import com.parking_reservation_system.dto.response.ReservationResponseDto;
import com.parking_reservation_system.model.Reservation;

public class ReservationMapper {

    // Request DTO -> Entity
    public static Reservation toEntity(ReservationDto dto) {
        if (dto == null) return null;

        Reservation reservation = new Reservation();
        reservation.setStartingTime(dto.startingTime());
        reservation.setEndingTime(dto.endingTime());
        reservation.setStatus(Reservation.Status.PENDING); 
        return reservation;
    }

    public static ReservationDto toDto(Reservation entity) {
        if (entity == null) return null;

        return new ReservationDto(
                entity.getId(),
                entity.getStartingTime(),
                entity.getEndingTime(),
                entity.getSlot().getId(),
                entity.getGarage().getId(),
                entity.getUser().getId()
        );
    }

    public static ReservationResponseDto toResponseDto(Reservation entity) {
        if (entity == null) return null;

        return new ReservationResponseDto(
                entity.getId(),
                entity.getStartingTime(),
                entity.getEndingTime(),
                entity.getSlot().getId(),
                entity.getGarage().getId(),
                entity.getUser().getId(),
                entity.getStatus().name() // convert enum to string
        );
    }
}
