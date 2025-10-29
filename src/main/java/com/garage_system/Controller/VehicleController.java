package com.garage_system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.garage_system.Model.Vehicle;
import com.garage_system.Service.VehicleService;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Create Vehicle
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleService.createVehicle(vehicle);
        return ResponseEntity.ok(createdVehicle);
    }

    // Update Vehicle
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVehicle(@RequestBody Vehicle vehicle, @PathVariable int id) {
        vehicleService.updateVehicle(vehicle, id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Delete Vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable int id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
// Get All Vehicles (with pagination and sorting)
@GetMapping
public ResponseEntity<Page<Vehicle>> getAllVehicles(
        @RequestParam int pageNo,
        @RequestParam int pageSize,
        @RequestParam(defaultValue = "id") String columnName,
        @RequestParam(defaultValue = "true") boolean isAsc) {

    Sort sort = isAsc
            ? Sort.by(columnName).ascending()
            : Sort.by(columnName).descending();

    PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

    
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
