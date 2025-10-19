package com.garage_system.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.DTO.ApiResponse;
import com.garage_system.Model.Vehicle;
import com.garage_system.Service.VehicleService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("vehicle")
public class VehicleController {

    private final VehicleService vehicleService ;

    @Autowired
    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService ;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createVehicle(@RequestBody Vehicle vehicle){
        Vehicle createdVehicle = vehicleService.createVehicle(vehicle) ;
        ApiResponse response = new ApiResponse<>(true , "Vehicle is created successfully" , createdVehicle , null);
        return ResponseEntity.ok(response);
    }
    
    /// update my vehicle info 
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateVehicle(@RequestBody Vehicle vehicle , @PathVariable("id") int id){
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicle , id) ;
        ApiResponse response   = new ApiResponse<>(true , "Vehicle is updated successfully" , updatedVehicle , null);
        return ResponseEntity.ok(response);

    }
    /// delete my vehicle info 
    /// 
    /// get all my vehicles 
    /// 
    /// get vehicle by id 
}
