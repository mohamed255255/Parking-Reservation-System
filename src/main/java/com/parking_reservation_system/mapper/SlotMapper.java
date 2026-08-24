package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.SlotRequest;
import com.parking_reservation_system.dto.response.SlotResponse;
import com.parking_reservation_system.model.Slot;

public final class SlotMapper {

    private SlotMapper() {}

    public static Slot toEntity(SlotRequest dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SlotRequest must not be null");
        }

        Slot slot = new Slot();
        slot.setSlotNumber(dto.slot_number());
        slot.setSlotWidth(dto.slotWidth());
        slot.setSlotDepth(dto.slotDepth());
        return slot;
    }

    public static SlotRequest toDto(Slot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot entity must not be null");
        }

        Integer garageId = (slot.getGarage() != null) ? slot.getGarage().getId() : null;
        Integer vehicleId = (slot.getVehicle() != null) ? slot.getVehicle().getId() : null;

        return new SlotRequest(
                slot.getSlotNumber(),
                slot.getSlotWidth(),
                slot.getSlotDepth(),
                garageId,
                vehicleId
        );
    }

    public static SlotResponse toResponseDto(Slot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot entity must not be null");
        }

        Integer garageId = (slot.getGarage() != null) ? slot.getGarage().getId() : null;
        Integer vehicleId = (slot.getVehicle() != null) ? slot.getVehicle().getId() : null;

        return new SlotResponse(
                slot.getSlotNumber(),
                slot.getSlotWidth(),
                slot.getSlotDepth(),
                garageId,
                vehicleId
        );
    }
}