package com.parking_reservation_system.service;

import com.parking_reservation_system.dto.request.VehicleRequest;
import com.parking_reservation_system.dto.response.VehicleResponse;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.VehicleMapper;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleResponse addVehicleToTheSystem(VehicleRequest VehicleRequest) {
        Vehicle newVehicle = VehicleMapper.toEntity(VehicleRequest);

        User currentAuthUser =
                ((CustomUserDetails)
                                SecurityContextHolder.getContext()
                                        .getAuthentication()
                                        .getPrincipal())
                        .getUser();

        newVehicle.setUser(currentAuthUser);
        var response = VehicleMapper.toResponseDto(newVehicle);

        return response;
    }

    public void updateVehicle(VehicleRequest VehicleRequest, int id) {
        vehicleRepository.updateVehicle(
                VehicleRequest.plateNumber(),
                VehicleRequest.modelYear(),
                VehicleRequest.modelName(),
                VehicleRequest.vehicleWidth(),
                VehicleRequest.vehicleDepth(),
                VehicleRequest.type().name(),
                id);
    }

    public void deleteVehicle(int id) {
        vehicleRepository.deleteVehicle(id);
    }

    public Page<VehicleResponse> getAllVehicles(PageRequest pageRequest) {
        return vehicleRepository.findAll(pageRequest).map(VehicleMapper::toResponseDto);
    }

    public VehicleResponse getVehicleById(int id) {
        return vehicleRepository
                .findById(id)
                .map(VehicleMapper::toResponseDto)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Vehicle not found with ID: %d" , id)));
    }
}
