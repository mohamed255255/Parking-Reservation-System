package com.garage_system.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.VehicleDto;
import com.garage_system.Model.Vehicle;
import com.garage_system.Repository.VehicleRepository;
import com.garage_system.mapper.VehicleMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VehicleService {

     private final VehicleRepository vehicleRepository ; 

     @Autowired
     public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository ;
     }
     
     public Vehicle createVehicle(VehicleDto vehicleDto){
          Vehicle newVehicle = VehicleMapper.toEntity(vehicleDto) ;
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
