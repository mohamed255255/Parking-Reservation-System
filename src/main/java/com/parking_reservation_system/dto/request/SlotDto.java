package com.parking_reservation_system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SlotDto(
        Integer slot_number,

        @NotNull
        @Positive(message = "Slot width must be positive")
        int slotWidth,

        @NotNull
        @Positive(message = "Slot depth must be positive")
        int slotDepth,

        Integer garage_id,
        Integer vehicle_id) {}
