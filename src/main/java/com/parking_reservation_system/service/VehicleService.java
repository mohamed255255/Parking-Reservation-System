package com.parking_reservation_system.service;
import java.util.List;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.dto.response.VehicleResponseDto;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.VehicleMapper;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VehicleService {

     private final VehicleRepository vehicleRepository ; 

     public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository ;
     }
     
     public VehicleResponseDto addVehicleToTheSystem(VehicleDto vehicleDto){
        Vehicle newVehicle = VehicleMapper.toEntity(vehicleDto) ;
        
        User currentAuthUser = ((CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();
        
        newVehicle.setUser(currentAuthUser);
        var response = VehicleMapper.toResponseDto(newVehicle);
      
        return response;
    }

   
    public void updateVehicle(VehicleDto vehicleDto , int id){
      vehicleRepository.updateVehicle(
            vehicleDto.plateNumber(),
            vehicleDto.modelYear(),
            vehicleDto.modelName(),
            vehicleDto.vehicleWidth(),
            vehicleDto.vehicleDepth(),
            vehicleDto.type().name(),
            id
        );    
    }

    public void deleteVehicle(int id){
        vehicleRepository.deleteVehicle(id);
    }

    
    public Page<VehicleResponseDto> getAllVehicles(PageRequest pageRequest) {
    return vehicleRepository.findAll(pageRequest)
            .map(VehicleMapper::toResponseDto);
    }


    public VehicleResponseDto getVehicleById(int id) {
        return vehicleRepository.findById(id).map(VehicleMapper::toResponseDto)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
    }
}
