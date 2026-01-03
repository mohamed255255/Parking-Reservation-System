package com.parking_reservation_system.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.AssertTrue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SlotDto {

    private int slot_number; 

    @NotNull
    @Positive(message = "Slot width must be positive")
    private Double slotWidth;
   
    @NotNull
    @Positive(message = "Slot depth must be positive")
    private Double slotDepth;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "Ending time is required")
    @Future(message = "Ending time must be in the future")
    private LocalDateTime endingTime;

    @AssertTrue(message = "Ending time must be after start time")
    public boolean isEndingTimeAfterStartTime() {
        if (startTime == null || endingTime == null) return true; 
        return endingTime.isAfter(startTime);
    }
    @NotNull(message = " slot should be connected with a garage")
    private int garage_id ;
    
    private int vehicle_id ;

}
