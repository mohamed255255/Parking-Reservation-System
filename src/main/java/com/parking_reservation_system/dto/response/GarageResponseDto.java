package com.parking_reservation_system.dto.response;

public record GarageResponseDto(

        int id,

        String name,

        String location,

        boolean isActive,

        int capacity

) {}
