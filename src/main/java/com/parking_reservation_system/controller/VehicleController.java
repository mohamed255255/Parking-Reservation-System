package com.parking_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.service.VehicleService;

@RestController
@RequestMapping("/vehicle")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Create Vehicle in the system
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleDto vehicleDto) {
        Vehicle createdVehicle = vehicleService.addVehicleToTheSystem(vehicleDto);
        return ResponseEntity.ok(createdVehicle);
    }
    
    // Update Vehicle
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVehicle(@RequestBody VehicleDto vehicleDto , @PathVariable int id) {
        vehicleService.updateVehicle(vehicleDto , id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Delete Vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable int id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Get All Vehicles in the system (with pagination and sorting)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Vehicle>> getAllVehicles(@RequestParam int pageNo  ,  @RequestParam int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Vehicle> vehicles = vehicleService.getAllVehicles(pageRequest);
        return ResponseEntity.ok(vehicles);
    }

    // Get Vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable int id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
}
