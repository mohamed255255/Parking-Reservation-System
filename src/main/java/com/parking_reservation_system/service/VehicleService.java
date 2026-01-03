package com.parking_reservation_system.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.mapper.VehicleMapper;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VehicleService {

     private final VehicleRepository vehicleRepository ; 

     @Autowired
     public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository ;
     }
     
     public Vehicle addVehicleToTheSystem(VehicleDto vehicleDto){
        Vehicle newVehicle = VehicleMapper.toEntity(vehicleDto) ;
        
        User currentAuthUser = ((CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();
        
        newVehicle.setUser(currentAuthUser);
        return vehicleRepository.save(newVehicle);
    }

   
    public void updateVehicle(VehicleDto vehicleDto , int id){
      vehicleRepository.updateVehicle(
            vehicleDto.getPlateNumber(),
            vehicleDto.getModelYear(),
            vehicleDto.getModelName(),
            vehicleDto.getVehicleWidth(),
            vehicleDto.getVehicleDepth(),
            vehicleDto.getType().name(),
            id
        );    
    }

    public void deleteVehicle(int id){
        vehicleRepository.deleteVehicle(id);
    }

    
    public Page<Vehicle> getAllVehicles(PageRequest pageRequest) {
        return vehicleRepository.findAll(pageRequest);
    }

    public Vehicle getVehicleById(int id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + id));
    }
}
