package com.parking_reservation_system.controller;

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

import com.parking_reservation_system.dto.request.GarageDto;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.service.GarageService;

import io.swagger.v3.oas.annotations.tags.Tag;

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
    public ResponseEntity<Garage> createGarage(@RequestBody GarageDto garageDto) {
        Garage createdGarage = garageService.createGarage(garageDto);
        return new ResponseEntity<>(createdGarage, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<Garage>> getAllGarages() {
        List<Garage> garages = garageService.getAllGaragesList();
        return ResponseEntity.ok(garages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Garage> getGarageById(@PathVariable int id) {
        Garage garage = garageService.getGarageById(id)
                .orElseThrow(() -> new RuntimeException("Garage not found with id: " + id));
        return ResponseEntity.ok(garage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Garage> updateGarage(@PathVariable int id, @RequestBody GarageDto garageDto) {
        Garage updated = garageService.updateGarage(id, garageDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable int id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}/slots")
    public ResponseEntity<List<Slot>> getSlotsForSpecificGarage(@PathVariable("id") int garageId) {
        List<Slot> relatedSlots = garageService.getSlotsForThatGarage(garageId);
        return ResponseEntity.ok(relatedSlots);
    }
}
