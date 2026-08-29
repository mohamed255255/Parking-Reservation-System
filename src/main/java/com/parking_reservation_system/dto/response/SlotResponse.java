package com.parking_reservation_system.dto.response;

public record SlotResponse(
        int slotNumber, double slotWidth, double slotDepth, int garageId, Integer vehicleId) {}
