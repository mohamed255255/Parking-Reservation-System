package com.garage_system.dto.request;

import java.time.LocalTime;

import com.garage_system.model.Reservation;

import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
  
    private int id ; 
    
    @Past(message = "you cant reserve in a previous date")
    @Nullable
    private LocalTime startingTime;
    
    @Nullable
    private LocalTime endigTime ;

    private int slot_id ;

    private int garage_id ;
    
    @NotNull(message = "user_id is required")  
    private int user_id ;

}
