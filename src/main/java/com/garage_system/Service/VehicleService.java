package com.garage_system.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garage_system.Model.Vehicle;
import com.garage_system.Repository.VehicleRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VehicleService {

     private final VehicleRepository vehicleRepository ; 

     @Autowired
     public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository ;
     }
     
    public Vehicle createVehicle(Vehicle vehicle){
          return vehicleRepository.save(vehicle);
    }

   
    public void updateVehicle(Vehicle v , int id){
      vehicleRepository.updateVehicle(
            v.getPlateNumber(),
            v.getModelYear(),
            v.getModelName(),
            v.getVehicleWidth(),
            v.getVehicleDepth(),
            v.getType().name(),
            id
        );    
    }

    public void deleteVehicle(int id){
        vehicleRepository.deleteVehicle(id);
    }

    
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(int id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + id));
    }
}
