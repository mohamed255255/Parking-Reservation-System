package com.garage_system.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.DTO.request.VehicleDto;
import com.garage_system.Model.Reservation;
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
              Optional<Reservation> newReservation = reservationService.createRequest(reservationDto , vehicleDto);                   
              if (newReservation.isEmpty())
                     return ResponseEntity.status(500).body("Failed to save the reservation");
              
              double price = reservationService.calculateFees(newReservation.get()) ;
              
              Map<String, Object> bill = new HashMap<>();
              bill.put("reservation", ReservationMapper.toDto(newReservation.get()));
              bill.put("price", price);

              return ResponseEntity.status(201).body(bill);

       }

       /// get my reservations as user paginated as user and support filters
       // @PreAuthorize("hasAnyRole('ADMIN')")
       /// get all reservations as admin in the system paginated and support filters for Admin

}
