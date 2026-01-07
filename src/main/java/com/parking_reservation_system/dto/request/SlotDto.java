package com.parking_reservation_system.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.AssertTrue;

public record SlotDto(

        int slot_number,

        @NotNull
        @Positive(message = "Slot width must be positive")
        Double slotWidth,

        @NotNull
        @Positive(message = "Slot depth must be positive")
        Double slotDepth,

        int garage_id,

        int vehicle_id

) {

}
