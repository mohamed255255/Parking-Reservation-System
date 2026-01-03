package com.parking_reservation_system.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GarageDto {
    private int id;
    
    @NotBlank(message =  "name can not be empty")
    private String name;
   
    @NotNull(message =  "location field is required")
    private String location;

    @NotNull(message =  "activity status is required")
    private boolean isActive;

    @Positive(message = "capacity of the garage shoud be positive value")
    @NotNull(message =  "Capacity is required")
    private int Capactiy ;
}
