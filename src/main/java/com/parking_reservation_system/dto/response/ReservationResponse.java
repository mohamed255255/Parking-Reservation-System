package com.parking_reservation_system.dto.response;

import java.time.LocalDateTime;

public record ReservationResponse(
        int id,
        LocalDateTime startingTime,
        LocalDateTime endingTime,
        int slotId,
        int garageId,
        int userId,
        String status) {}
