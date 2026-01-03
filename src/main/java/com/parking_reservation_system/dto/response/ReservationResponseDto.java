package com.parking_reservation_system.dto.response;

import java.time.LocalTime;

public record ReservationResponseDto(
        int id,
        LocalTime startingTime,
        LocalTime endingTime,
        int slotId,
        int garageId,
        int userId,
        String status // Reservation.Status enum converted to string
) {}
