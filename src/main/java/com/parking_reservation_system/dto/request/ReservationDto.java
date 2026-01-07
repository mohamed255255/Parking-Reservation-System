package com.parking_reservation_system.dto.request;

import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record ReservationDto(

        int id,

        @Past(message = "you cant reserve in a previous date")
        LocalTime startingTime,

        LocalTime endingTime,

        int slot_id,

        int garage_id

) {}
