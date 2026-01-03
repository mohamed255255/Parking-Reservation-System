package com.parking_reservation_system.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.parking_reservation_system.dto.request.SlotDto;
import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.service.QRCodeService;
import com.parking_reservation_system.service.SlotService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/slots")
@AllArgsConstructor
@Tag(name = "Slot", description = "Slot CRUD for Admin")
public class SlotController {
    
    private final SlotService slotService;
   
    @PostMapping
    public ResponseEntity<?> createSlot(@RequestBody SlotDto slotDto) throws WriterException , IOException{
        var slotResponseDto = slotService.createSlot(slotDto);
        return ResponseEntity.ok(slotResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSlotById(@PathVariable("id") int id) {
        var slotResponseDto = slotService.getSlotById(id);
        return ResponseEntity.ok(slotResponseDto);
    }

}
