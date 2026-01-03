package com.parking_reservation_system.dto.response;

import java.time.LocalDateTime;

public record SlotResponseDto(
        int slotNumber,
        double slotWidth,
        double slotDepth,
        LocalDateTime startTime,
        LocalDateTime endingTime,
        int garageId,
        Integer vehicleId // can be null if no vehicle assigned
) {}
