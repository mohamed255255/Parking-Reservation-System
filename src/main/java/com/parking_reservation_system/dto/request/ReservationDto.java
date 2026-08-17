package com.parking_reservation_system.dto.request;

import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;

public record ReservationDto(
        Integer id,
        @Past(message = "you can't reserve in a previous date") LocalDateTime startingTime,
        LocalDateTime endingTime,
        Integer slot_id,
        Integer garage_id) {}
