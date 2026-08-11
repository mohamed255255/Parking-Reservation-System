package com.parking_reservation_system.controller;

import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.dto.response.VehicleResponseDto;
import com.parking_reservation_system.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody VehicleDto vehicleDto) {
        VehicleResponseDto createdVehicle = vehicleService.addVehicleToTheSystem(vehicleDto);
        return ResponseEntity.ok(createdVehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(
            @RequestBody VehicleDto vehicleDto, @PathVariable int id) {
        vehicleService.updateVehicle(vehicleDto, id);
        return ResponseEntity.ok("Vehicle is updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable int id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle is deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<?>> getAllVehicles(
            @RequestParam int pageNo, @RequestParam int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<VehicleResponseDto> vehicles = vehicleService.getAllVehicles(pageRequest);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable int id) {
        VehicleResponseDto vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
}
