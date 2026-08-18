package com.parking_reservation_system.dto.request;

import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;

/// TODO : put more constraints on DTOs , but start time is non null ,
//  but ending time can be nullable or open until user stop it
// this dto could have request suffix ex : ReservationRequest and remove suffix DTO 
public record ReservationDto(
        Integer id,
        @Past(message = "you can't reserve in a previous date") LocalDateTime startingTime,
        LocalDateTime endingTime,
        Integer slot_id,  
        Integer garage_id) {}  
