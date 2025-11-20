package com.garage_system.mapper;

import com.garage_system.DTO.request.SlotDto;
import com.garage_system.Model.Slot;

public class SlotMapper {

    public static Slot toEntity(SlotDto dto) {

        Slot slot = new Slot();
        slot.setId(dto.getId());
        slot.setSlotWidth(dto.getSlotWidth());
        slot.setSlotDepth(dto.getSlotDepth());
        slot.setEmpty(dto.getEmpty());
        slot.setStartTime(dto.getStartTime());
        slot.setEndingTime(dto.getEndingTime());

        return slot;
    }

    public static SlotDto toDto(Slot slot) {
        if (slot == null) return null;

        SlotDto dto = new SlotDto();
        dto.setId(slot.getId());
        dto.setSlotWidth(slot.getSlotWidth());
        dto.setSlotDepth(slot.getSlotDepth());
        dto.setEmpty(slot.getisEmpty());
        dto.setStartTime(slot.getStartTime());
        dto.setEndingTime(slot.getEndingTime());

        return dto;
    }
}
