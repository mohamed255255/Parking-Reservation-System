package com.garage_system.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.DTO.request.VehicleDto;
import com.garage_system.Service.ReservationService;
import com.garage_system.Service.SlotService;
import com.garage_system.mapper.ReservationMapper;

@RestController
@RequestMapping("/reservation")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class ReservationController {

       private final ReservationService reservationService;
       private final SlotService slotService ;

       public ReservationController(ReservationService reservationService , SlotService slotService) {
              this.reservationService = reservationService;
              this.slotService = slotService ;
       }

       @PreAuthorize("hasAnyRole('USER')")
       @PostMapping
       public ResponseEntity<?> createRequest(@RequestBody ReservationDto reservationDto , @RequestBody VehicleDto vehicleDto) {
              var result = reservationService.createRequest(reservationDto , vehicleDto);             
              if (result.isEmpty())
                     return ResponseEntity.status(500).body("Failed to save the reservation");
              return ResponseEntity
                            .status(201)
                            .body(ReservationMapper.toDto(result.get()));
       }

       /// get my reservations as user paginated as user and support filters
       // @PreAuthorize("hasAnyRole('ADMIN')")
       /// get all reservations as admin in the system paginated and support filters for Admin

}
