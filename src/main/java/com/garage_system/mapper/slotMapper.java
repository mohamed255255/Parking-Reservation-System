package com.garage_system.mapper;

import com.garage_system.dto.request.SlotDto;
import com.garage_system.model.Slot;

public class SlotMapper {
     public static Slot toEntity(SlotDto dto) {

        Slot slot = new Slot();
        slot.setSlotNumber(dto.getSlot_number());
        slot.setSlotWidth(dto.getSlotWidth());
        slot.setSlotDepth(dto.getSlotDepth());
        slot.setStartTime(dto.getStartTime());
        slot.setEndingTime(dto.getEndingTime());
        return slot;
    }

    public static SlotDto toDto(Slot slot) {
        if (slot == null) return null;
    
        SlotDto dto = new SlotDto();
        dto.setSlot_number(slot.getSlotNumber());
        dto.setSlotWidth(slot.getSlotWidth());
        dto.setSlotDepth(slot.getSlotDepth());
        dto.setStartTime(slot.getStartTime());
        dto.setEndingTime(slot.getEndingTime());

        return dto;
    }
}
