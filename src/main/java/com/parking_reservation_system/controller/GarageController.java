package com.parking_reservation_system.controller;

import com.parking_reservation_system.dto.request.GarageRequest;
import com.parking_reservation_system.dto.response.GarageResponse;
import com.parking_reservation_system.dto.response.SlotResponse;
import com.parking_reservation_system.service.GarageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Garage", description = "Garage CRUD for Admin")
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/garages")
public class GarageController {

    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<GarageResponse> createGarage(@RequestBody GarageRequest GarageRequest) {
        GarageResponse createdGarage = garageService.createGarage(GarageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGarage);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllGarages() {
        List<GarageResponse> garages = garageService.getAllGaragesList();
        return ResponseEntity.ok(garages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGarageById(@PathVariable int id) {
        GarageResponse garage = garageService.getGarageById(id);
        return ResponseEntity.ok(garage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGarage(
            @PathVariable int id, @RequestBody GarageRequest GarageRequest) {
        GarageResponse updatedGarage = garageService.updateGarage(id, GarageRequest);
        return ResponseEntity.ok(updatedGarage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGarage(@PathVariable int id) {
        garageService.deleteGarage(id);
        return ResponseEntity.ok("garage is deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}/slots")
    public ResponseEntity<?> getSlotsForSpecificGarage(@PathVariable("id") int garageId) {
        List<SlotResponse> relatedSlots = garageService.getSlotsForThatGarage(garageId);
        return ResponseEntity.ok(relatedSlots);
    }
}
