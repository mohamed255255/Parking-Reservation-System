package com.parking_reservation_system.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.parking_reservation_system.dto.request.ReservationDto;
import com.parking_reservation_system.mapper.ReservationMapper;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.security.CustomUserDetails;
import com.parking_reservation_system.service.ReservationService;

@RestController
@RequestMapping("/reservation")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class ReservationController {

       private final ReservationService reservationService;

       public ReservationController(ReservationService reservationService) {
              this.reservationService = reservationService;
       }

       @PreAuthorize("hasAnyRole('USER')")
       @PostMapping("/{vehicleId}")
       public ResponseEntity<?> createRequest(@AuthenticationPrincipal CustomUserDetails userDetails , @RequestBody ReservationDto reservationDto , @PathVariable int vehicleId) {
              
              Optional<Reservation> newReservation = reservationService.createRequest(userDetails , reservationDto , vehicleId);                   
             
              if (newReservation.isEmpty())
                     return ResponseEntity.status(500).body("Failed to save the reservation");
              
              double price = reservationService.calculateFees(newReservation.get()) ;
              
              Map<String, Object> bill = new HashMap<>();
              bill.put("reservation", ReservationMapper.toResponseDto(newReservation.get()));
              bill.put("price", price);

              return ResponseEntity.status(201).body(bill);
       }
 

       @PreAuthorize("hasAnyRole('USER')")
       @PostMapping("/confirmation")
       public ResponseEntity<?> confirmReservation(@RequestParam("file") MultipartFile  file) throws IOException{
          String IFrameLink = reservationService.confirmReservation(file.getBytes()) ;
          Map<String, String> response = new HashMap<>();
          response.put("message", "Your reservation has been confirmed successfully, now your parking time started");
          response.put("paymentLink", IFrameLink);

          return ResponseEntity.ok(response);
       }

       /// get my reservations as user paginated as user and support filters
       /// @PreAuthorize("hasAnyRole('ADMIN')")
       /// get all reservations as admin in the system paginated and support filters for Admin

}
