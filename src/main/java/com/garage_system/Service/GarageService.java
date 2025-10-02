package com.garage_system.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.garage_system.DTO.ApiResponse;
import com.garage_system.Model.Garage;
import com.garage_system.Model.Slot;
import com.garage_system.Repository.GarageRepository;

@Service
public class GarageService {
    
    private final GarageRepository garageRepository;

    @Autowired
    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    public Garage createGarage(Garage garage) {
        return garageRepository.save(garage);
    }

    public ResponseEntity< ApiResponse< List<Garage> >> getAllGarages() {
        List<Garage> garages = garageRepository.getAllGarages();
        ApiResponse<List<Garage>> response = new ApiResponse<List<Garage>>(
            true, 
            "Garages retrieved successfully", 
            garages, 
            null
            );
        return ResponseEntity.ok(response);
    }

    public Optional<Garage> getGarageById(int id) {
        return garageRepository.findById(id);
    }

    public Garage updateGarage(int id, Garage updatedGarage) {
        return garageRepository.findById(id)
            .map(existing -> {
                existing.setName(updatedGarage.getName());
                existing.setLocation(updatedGarage.getLocation());
                existing.setActive(updatedGarage.isActive());
                return garageRepository.save(existing);
            })
            .orElseThrow(() -> new IllegalArgumentException("Garage with id " + id + " not found"));
    }

    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }

   public List<Slot> getSlotsForThatGarage(int garageId){
        var foundSlots = garageRepository.findAllSlots(garageId);
        if(foundSlots.isEmpty()){
            throw new RuntimeException("No slots found for garage with id: " + garageId);
        }
        return foundSlots ;
    }
 
}
