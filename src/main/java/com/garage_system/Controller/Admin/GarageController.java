package com.garage_system.Controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.garage_system.DTO.ApiResponse;
import com.garage_system.Model.Garage;
import com.garage_system.Service.Admin.GarageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Garage", description = "Garage CRUD for Admin")
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/garages")
public class GarageController {
    
    private final GarageService garageService;

    @Autowired
    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Garage>> createGarage(@RequestBody Garage garage) {
        Garage createdGarage = garageService.createGarage(garage);
        ApiResponse<Garage> response = new ApiResponse<>(
            true, 
            "Garage created successfully", 
            createdGarage, 
            null
            );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Garage>>> getAllGarages() {
        return garageService.getAllGarages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Garage>> getGarageById(@PathVariable int id) {
        var garage = garageService.getGarageById(id)
                .orElseThrow(() -> new RuntimeException("Garage not found with id: " + id));

        var response = new ApiResponse<>(
                true,
                "Garage with id " + id + " is found",
                garage,
                null
        );
        return ResponseEntity.ok(response);
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
}
