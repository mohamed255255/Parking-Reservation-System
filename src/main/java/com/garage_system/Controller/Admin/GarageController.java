package com.garage_system.Controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.Model.ApiResponse;
import com.garage_system.Model.Garage;
import com.garage_system.Service.Admin.GarageService;

@RestController
@RequestMapping("/garages")
public class GarageController {
    
    private final GarageService garageService;

    @Autowired
    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<Garage> createGarage(@RequestBody Garage garage) {
        Garage created = garageService.createGarage(garage);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Garage>>> getAllGarages() {
        return garageService.getAllGarages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Garage> getGarageById(@PathVariable int id) {
        return garageService.getGarageById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
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
