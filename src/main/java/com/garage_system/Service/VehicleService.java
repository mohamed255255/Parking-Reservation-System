package com.garage_system.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garage_system.Model.Vehicle;
import com.garage_system.Repository.VehicleRepository;

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

   
    public Vehicle updateVehicle(Vehicle v , int id){

           Vehicle updatedVehicle =  vehicleRepository.findById(id).orElseThrow();
           updatedVehicle.setPlateNumber(v.getPlateNumber());
           updatedVehicle.setModelYear(v.getModelYear());
           updatedVehicle.setVehicleWidth(v.getVehicleWidth());
           updatedVehicle.setVehicleDepth(v.getVehicleDepth());
           updatedVehicle.setType(v.getType());
           return vehicleRepository.save(updatedVehicle);
    }
}
