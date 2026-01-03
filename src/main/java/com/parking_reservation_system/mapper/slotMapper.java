package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.SlotDto;
import com.parking_reservation_system.dto.response.SlotResponseDto;
import com.parking_reservation_system.model.Slot;

public class SlotMapper {

    // Request DTO -> Entity
    public static Slot toEntity(SlotDto dto) {
        if (dto == null) return null;

        Slot slot = new Slot();
        slot.setSlotNumber(dto.slot_number());
        slot.setSlotWidth(dto.slotWidth());
        slot.setSlotDepth(dto.slotDepth());
        slot.setStartTime(dto.startTime());
        slot.setEndingTime(dto.endingTime());
        // If needed, set slot.setGarage(...) or slot.setVehicle(...)
        return slot;
    }

    // Entity -> Request DTO (if you still need it)
    public static SlotDto toDto(Slot slot) {
        if (slot == null) return null;

        return new SlotDto(
                slot.getSlotNumber(),
                slot.getSlotWidth(),
                slot.getSlotDepth(),
                slot.getStartTime(),
                slot.getEndingTime(),
                slot.getGarage().getId(),
                slot.getVehicle() != null ? slot.getVehicle().getId() : 0
        );
    }

    // Entity -> Response DTO
    public static SlotResponseDto toResponseDto(Slot slot) {
        if (slot == null) return null;

        return new SlotResponseDto(
                slot.getSlotNumber(),
                slot.getSlotWidth(),
                slot.getSlotDepth(),
                slot.getStartTime(),
                slot.getEndingTime(),
                slot.getGarage().getId(),
                slot.getVehicle() != null ? slot.getVehicle().getId() : null
        );
    }
}
