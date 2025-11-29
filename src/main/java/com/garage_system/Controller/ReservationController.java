package com.garage_system.Controller;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Service.ReservationService;
import com.garage_system.mapper.ReservationMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/reservation")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class ReservationController {

       private final ReservationService reservationService;

       public ReservationController(ReservationService reservationService) {
              this.reservationService = reservationService;
       }

       /// create reseration for user
       @PostMapping
       public ResponseEntity<?> createRequest(@RequestBody ReservationDto reservationDto) {
              var result = reservationService.createRequest(reservationDto);
              if (result.isEmpty())
                     return ResponseEntity.status(409).body("conflict");

              return ResponseEntity
                            .status(201)
                            .body(ReservationMapper.toDto(result.get()));
       }

       /// get my reservations as user

       /// see all reservations paginated as user and support filters

}
