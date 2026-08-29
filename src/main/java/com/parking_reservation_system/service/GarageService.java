package com.parking_reservation_system.service;

import com.parking_reservation_system.dto.request.GarageRequest;
import com.parking_reservation_system.dto.response.GarageResponse;
import com.parking_reservation_system.dto.response.SlotResponse;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.GarageMapper;
import com.parking_reservation_system.mapper.SlotMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.repository.GarageRepository;
import com.parking_reservation_system.repository.SlotRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GarageService {

    private final GarageRepository garageRepository;
    private final SlotRepository slotRepository;

    public GarageResponse createGarage(GarageRequest GarageRequest) {
        Garage garage = GarageMapper.toEntity(GarageRequest);
        var createdGarage = GarageMapper.toResponseDto(garageRepository.save(garage));
        return createdGarage;
    }

    public List<GarageResponse> getAllGaragesList() {
        return garageRepository.findAll().stream()
                .map(garage -> GarageMapper.toResponseDto(garage))
                .toList();
    }

    public GarageResponse getGarageById(int id) {
        return garageRepository
                .findById(id)
                .map(garage -> GarageMapper.toResponseDto(garage))
                .orElseThrow(
                        () -> {
                            throw new ResourceNotFoundException(
                                   String.format(" garage of id %d is not found", id));
                        });
    }

    public GarageResponse updateGarage(int id, GarageRequest GarageRequest) {
        Garage updatedGarage =
                garageRepository
                        .findById(id)
                        .map(
                                existing -> {
                                    existing.setName(GarageRequest.name());
                                    existing.setLocation(GarageRequest.location());
                                    existing.setActive(GarageRequest.isActive());
                                    return garageRepository.save(existing);
                                })
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                    String.format(" garage of id %d is not found", id)));

        return GarageMapper.toResponseDto(updatedGarage);
    }

    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }

    public List<SlotResponse> getSlotsForThatGarage(int garageId) {
        return slotRepository.findAllSlots(garageId).stream()
                .map(slot -> SlotMapper.toResponseDto(slot))
                .toList();
    }
}
