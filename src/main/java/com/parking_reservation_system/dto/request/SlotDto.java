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

        @NotNull(message = "Start time is required")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @NotNull(message = "Ending time is required")
        @Future(message = "Ending time must be in the future")
        LocalDateTime endingTime,

        int garage_id,

        int vehicle_id

) {

    @AssertTrue(message = "Ending time must be after start time")
    public boolean isEndingTimeAfterStartTime() {
        if (startTime == null || endingTime == null) return true;
        return endingTime.isAfter(startTime);
    }
}
