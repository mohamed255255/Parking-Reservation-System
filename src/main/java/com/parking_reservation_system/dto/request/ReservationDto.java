package com.parking_reservation_system.dto.request;

import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record ReservationDto(

        Integer id,

        @Past(message = "you can't reserve in a previous date")
        LocalDateTime startingTime,


        LocalDateTime endingTime,

        Integer slot_id,

        Integer garage_id

) {}
