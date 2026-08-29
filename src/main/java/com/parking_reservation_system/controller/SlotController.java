package com.parking_reservation_system.controller;

import com.parking_reservation_system.dto.request.SlotRequest;
import com.parking_reservation_system.dto.response.SlotResponse;
import com.parking_reservation_system.service.SlotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/slots")
@AllArgsConstructor
@Tag(name = "Slot", description = "Slot CRUD for Admin")
public class SlotController {

    private final SlotService slotService;

    @PostMapping
    public ResponseEntity<SlotResponse> createSlot(@RequestBody SlotRequest SlotRequest) {
        var SlotResponse = slotService.createSlot(SlotRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(SlotResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<SlotResponse> getSlotById(@PathVariable("id") int id) {
        SlotResponse SlotResponse = slotService.getSlotById(id);
        return ResponseEntity.ok(SlotResponse);
    }
}
