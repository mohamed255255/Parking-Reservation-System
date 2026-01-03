package com.parking_reservation_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking_reservation_system.dto.request.GarageDto;
import com.parking_reservation_system.dto.response.GarageResponseDto;
import com.parking_reservation_system.dto.response.SlotResponseDto;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.GarageMapper;
import com.parking_reservation_system.mapper.SlotMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.repository.GarageRepository;
@Service
public class GarageService {
    
    private final GarageRepository garageRepository;

    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    public GarageResponseDto createGarage(GarageDto garageDto) {
        Garage garage     = GarageMapper.toEntity(garageDto) ;
        var createdGarage = GarageMapper.toResponseDto(
            garageRepository.save(garage)
        );
        return createdGarage;
    }

    public List<GarageResponseDto> getAllGaragesList() {
    return garageRepository.getAllGarages().stream()
            .map(garage -> GarageMapper.toResponseDto(garage)) 
            .toList(); 
    }


    public GarageResponseDto getGarageById(int id) {
        return garageRepository.findById(id)
        .map( garage -> GarageMapper.toResponseDto(garage)).orElseThrow(
            () -> {throw new ResourceNotFoundException("garage of id" + id + "is not found");});
      
    }

    public GarageResponseDto updateGarage(int id, GarageDto garageDto) {
    Garage updatedGarage = garageRepository.findById(id)
            .map(existing -> {
                existing.setName(garageDto.name());
                existing.setLocation(garageDto.location());
                existing.setActive(garageDto.isActive());
                return garageRepository.save(existing); 
            })
            .orElseThrow(() -> new ResourceNotFoundException("Garage with id " + id + " not found"));

       return GarageMapper.toResponseDto(updatedGarage);
    } 
    
    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }

    public List<SlotResponseDto> getSlotsForThatGarage(int garageId) {
         return garageRepository.findAllSlots(garageId)
        .stream()
        .map( slot -> SlotMapper.toResponseDto(slot))
        .toList(); 
    }
}
