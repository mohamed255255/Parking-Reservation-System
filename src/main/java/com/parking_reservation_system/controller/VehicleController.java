package com.parking_reservation_system.controller;

import com.parking_reservation_system.dto.request.VehicleRequest;
import com.parking_reservation_system.dto.response.VehicleResponse;
import com.parking_reservation_system.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicle")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(
            @RequestBody VehicleRequest VehicleRequest) {
        VehicleResponse createdVehicle = vehicleService.addVehicleToTheSystem(VehicleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVehicle(
            @RequestBody VehicleRequest VehicleRequest, @PathVariable int id) {
        vehicleService.updateVehicle(VehicleRequest, id);
        return ResponseEntity.ok("Vehicle is updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable int id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle is deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<VehicleResponse>> getAllVehicles(
            @RequestParam int pageNo, @RequestParam int pageSize) {
        Page<VehicleResponse> vehicles =
                vehicleService.getAllVehicles(PageRequest.of(pageNo, pageSize));
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable int id) {
        VehicleResponse vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
}
