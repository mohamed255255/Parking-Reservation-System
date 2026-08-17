package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.SlotDto;
import com.parking_reservation_system.dto.response.SlotResponseDto;
import com.parking_reservation_system.model.Slot;

public class SlotMapper {

    public static Slot toEntity(SlotDto dto) {
        if (dto == null) return null;

        Slot slot = new Slot();
        slot.setSlotNumber(dto.slot_number());
        slot.setSlotWidth(dto.slotWidth());
        slot.setSlotDepth(dto.slotDepth());
        return slot;
    }

    public static SlotDto toDto(Slot slot) {
        if (slot == null) return null;

        return new SlotDto(
                slot.getSlotNumber(),
                slot.getSlotWidth(),
                slot.getSlotDepth(),
                slot.getGarage().getId(),
                slot.getVehicle() != null ? slot.getVehicle().getId() : 0);
    }

    public static SlotResponseDto toResponseDto(Slot slot) {
        if (slot == null) return null;

        return new SlotResponseDto(
                slot.getSlotNumber(),
                slot.getSlotWidth(),
                slot.getSlotDepth(),
                slot.getGarage().getId(),
                slot.getVehicle() != null ? slot.getVehicle().getId() : null);
    }
}
