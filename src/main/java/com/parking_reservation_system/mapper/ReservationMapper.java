package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.ReservationUserRequest;
import com.parking_reservation_system.dto.response.ReservationResponse;
import com.parking_reservation_system.model.Reservation;

public final class ReservationMapper {

    private ReservationMapper() {}

    public static Reservation toEntity(ReservationUserRequest dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ReservationUserRequest must not be null");
        }

        Reservation reservation = new Reservation();
        reservation.setStartingTime(dto.startingTime());
        reservation.setEndingTime(dto.endingTime());
        reservation.setStatus(Reservation.Status.PENDING);
        return reservation;
    }

    public static ReservationResponse toResponseDto(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation reservation must not be null");
        }

        Integer slotId = (reservation.getSlot() != null) ? reservation.getSlot().getId() : null;
        Integer garageId =
                (reservation.getGarage() != null) ? reservation.getGarage().getId() : null;
        Integer userId = (reservation.getUser() != null) ? reservation.getUser().getId() : null;
        String status = (reservation.getStatus() != null) ? reservation.getStatus().name() : null;

        return new ReservationResponse(
                reservation.getId(),
                reservation.getStartingTime(),
                reservation.getEndingTime(),
                slotId,
                garageId,
                userId,
                status);
    }
}
