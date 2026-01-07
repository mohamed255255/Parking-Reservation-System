package com.parking_reservation_system.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.AssertTrue;

public record SlotDto(

        Integer slot_number,

        @NotNull
        @Positive(message = "Slot width must be positive")
        Double slotWidth,

        @NotNull
        @Positive(message = "Slot depth must be positive")
        Double slotDepth,

        Integer garage_id,

        Integer vehicle_id

) {

}
