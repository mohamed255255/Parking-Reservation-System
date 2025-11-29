package com.garage_system.DTO.request;

import java.time.LocalTime;

import jakarta.validation.constraints.Future;
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
    private LocalTime startingTime;

}
