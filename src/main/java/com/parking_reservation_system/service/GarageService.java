package com.parking_reservation_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking_reservation_system.dto.request.GarageDto;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.GarageMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.repository.GarageRepository;
@Service
public class GarageService {
    
    private final GarageRepository garageRepository;

    @Autowired
    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    public Garage createGarage(GarageDto garageDto) {
        Garage garage = GarageMapper.toEntity(garageDto) ;
        return garageRepository.save(garage);
    }

    public List<Garage> getAllGaragesList() {
        return garageRepository.getAllGarages();
    }

    public Optional<Garage> getGarageById(int id) {
        return garageRepository.findById(id);
    }

    public Garage updateGarage(int id, GarageDto garageDto) {
        return garageRepository.findById(id)
            .map(existing -> {
                existing.setName(garageDto.getName());
                existing.setLocation(garageDto.getLocation());
                existing.setActive(garageDto.isActive());
                return garageRepository.save(existing);
            })
            .orElseThrow(() -> new ResourceNotFoundException("Garage with id " + id + " not found"));
    }

    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }

    public List<Slot> getSlotsForThatGarage(int garageId) {
        List<Slot> foundSlots = garageRepository.findAllSlots(garageId);
        if (foundSlots.isEmpty()) {
            throw new RuntimeException("No slots found for garage with id: " + garageId);
        }
        return foundSlots;
    }
}
