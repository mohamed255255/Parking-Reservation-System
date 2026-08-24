package com.parking_reservation_system.dto.request;

import com.beust.jcommander.internal.Nullable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SlotRequest(
        Integer slot_number,

        @NotNull
        @Positive(message = "Slot width must be positive")
        int slotWidth,

        @NotNull
        @Positive(message = "Slot depth must be positive")
        int slotDepth,

        @Nullable
        Integer garageId,
        
        @Nullable
        Integer vehicleId) {}
