package com.parking_reservation_system.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReservationUserRequest(
        @NotNull(message = "Starting time is required")
                @FutureOrPresent(message = "You can't reserve in a previous date")
                LocalDateTime startingTime,
        @Nullable LocalDateTime endingTime,
        @NotNull(message = "Slot ID is required") Integer slotId,
        @NotNull(message = "Garage ID is required") Integer garageId) {}
