package com.parking_reservation_system.dto.response;

public record GarageResponse(
         int id,
         String name, 
         String location, 
         boolean isActive, 
         int capacity) {}
