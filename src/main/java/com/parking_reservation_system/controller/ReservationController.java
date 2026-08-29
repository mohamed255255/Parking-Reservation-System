package com.parking_reservation_system.controller;

import com.parking_reservation_system.dto.request.ReservationUserRequest;
import com.parking_reservation_system.dto.response.ApiResponse;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.security.CustomUserDetails;
import com.parking_reservation_system.service.ReservationService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<?>> createReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReservationUserRequest reservationUserRequest,
            @PathVariable int vehicleId) {

        Map<String, Object> reservationBill =
                reservationService.createReservation(userDetails, reservationUserRequest, vehicleId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(reservationBill));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/confirmation")
    public ResponseEntity<ApiResponse<?>> confirmReservation(
            @RequestParam("file") MultipartFile file) throws IOException {

        Map<String, Object> confirmationInformation =
                reservationService.confirmReservation(file.getBytes());

        return ResponseEntity.ok(ApiResponse.success(confirmationInformation));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<?>> getUserReservations(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer slotId,
            @RequestParam(required = false) Integer garageId,
            @RequestParam(required = false) Reservation.Status status,
            @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startingTime,
            @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endingTime) {

        var result =
                reservationService.getUserReservations(
                        user.getId(), slotId, garageId, status, startingTime, endingTime, page, size);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer slotId,
            @RequestParam(required = false) Integer garageId,
            @RequestParam(required = false) Reservation.Status status,
            @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startingTime,
            @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endingTime) {

        var result =
                reservationService.getAllReservations(
                        slotId, garageId, status, startingTime, endingTime, page, size);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateReservation(
            @PathVariable Integer id, @RequestBody ReservationUserRequest dto) {
        var updated = reservationService.updateReservation(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }
}