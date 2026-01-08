package com.parking_reservation_system.dto.response;

import java.time.LocalDateTime;

public record ReservationResponseDto(
        int id,
        LocalDateTime startingTime,
        LocalDateTime endingTime,
        int slotId,
        int garageId,
        int userId,
        String status // Reservation.Status enum converted to string
) {}
