package com.garage_system.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.garage_system.Model.Garage;
import com.garage_system.Model.Slot;
import com.garage_system.Service.GarageService;

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
    public ResponseEntity<Garage> createGarage(@RequestBody Garage garage) {
        Garage createdGarage = garageService.createGarage(garage);
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
    public ResponseEntity<Garage> updateGarage(@PathVariable int id, @RequestBody Garage garage) {
        Garage updated = garageService.updateGarage(id, garage);
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
