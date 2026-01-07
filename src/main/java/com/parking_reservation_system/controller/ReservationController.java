package com.parking_reservation_system.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.parking_reservation_system.dto.request.ReservationDto;
import com.parking_reservation_system.dto.response.ReservationResponseDto;
import com.parking_reservation_system.mapper.ReservationMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.security.CustomUserDetails;
import com.parking_reservation_system.service.ReservationService;
import com.parking_reservation_system.dto.response.ApiResponse ;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ch.qos.logback.core.model.processor.PhaseIndicator;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;


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
       public ApiResponse<?> createReservation(@RequestBody ReservationDto reservationDto , @PathVariable int vehicleId) {
            
              ReservationResponseDto newReservation = reservationService.createReservation(reservationDto , vehicleId);                   
              double price  = reservationService.calculateFees(newReservation) ;
              
              Map<String, Object> bill = new HashMap<>();
              bill.put("reservation", newReservation);
              bill.put("price", price);
              return ApiResponse.success(bill);

       }

       @PreAuthorize("hasAnyRole('USER')")
       @PostMapping("/confirmation")
       public ApiResponse<?> confirmReservation(@RequestParam("file") MultipartFile  file) throws IOException{
          reservationService.confirmReservation(file.getBytes()) ;
          return ApiResponse.success("reservation has been confirmed , redirect to the payment page");
       }

       @PreAuthorize("hasAnyRole('USER')")
       @GetMapping("/user")
       public ApiResponse<?> getUserReservations(
              @AuthenticationPrincipal CustomUserDetails user,
              @RequestParam(defaultValue = "0") int page,
              @RequestParam(defaultValue = "10") int size,
              @RequestParam(required = false) int slot_id,
              @RequestParam(required = false) int garage_id,
              @RequestParam(required = false) Reservation.Status status,
              @RequestParam(required = false) LocalDateTime startingTime,
              @RequestParam(required = false) LocalDateTime endingTime) {
                     
              return ApiResponse.success(reservationService.getUserReservations(
                     user.getId(),
                     slot_id,
                     garage_id,
                     status,
                     startingTime,
                     endingTime,
                     page,
                     size
              ));
       }
       
     
       // @PreAuthorize("hasAnyRole('ADMIN')")
       // get all reservations as admin in the system paginated and support filters for Admin
      
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ApiResponse<?> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer slot_id,
            @RequestParam(required = false) Integer garage_id,
            @RequestParam(required = false) Reservation.Status status,
            @RequestParam(required = false) LocalDateTime startingTime,
            @RequestParam(required = false) LocalDateTime endingTime
    ) {
        return ApiResponse.success(
                reservationService.getAllReservations(
                        slot_id,
                        garage_id,
                        status,
                        startingTime,
                        endingTime,
                        page,
                        size
                )
        );
    }
      
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ApiResponse.success("Reservation deleted successfully");
    }

    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    public ApiResponse<?> updateReservation(
            @PathVariable Integer id,
            @RequestBody ReservationDto dto
    ) {
        return ApiResponse.success(
                reservationService.patchReservation(id, dto)
        );
    }
}
