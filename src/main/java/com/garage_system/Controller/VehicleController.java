package com.garage_system.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ApiResponse<Vehicle>> createVehicle(@RequestBody Vehicle vehicle){
        Vehicle createdVehicle = vehicleService.createVehicle(vehicle) ;
        ApiResponse<Vehicle> response = new ApiResponse<>(true , "Vehicle is created successfully" , createdVehicle , null);
        return ResponseEntity.ok(response);
    }
    

   @PutMapping("/{id}")
   public ResponseEntity<ApiResponse<Void>> updateVehicle(@RequestBody Vehicle vehicle, @PathVariable int id) {
       vehicleService.updateVehicle(vehicle, id);
       ApiResponse<Void> response = new ApiResponse<>(true, "Vehicle updated successfully", null, null);
       return ResponseEntity.ok(response);
   }

   
   @DeleteMapping("/{id}")
   public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable int id) {
       vehicleService.deleteVehicle(id);
       ApiResponse<Void> response = new ApiResponse<>(true, "Vehicle deleted successfully", null, null);
       return ResponseEntity.ok(response);
   }

  @GetMapping
  public ResponseEntity<ApiResponse<List<Vehicle>>> getAllVehicles(@RequestParam int pageNo , @RequestParam int pageSize) {
      List<Vehicle> vehicles = vehicleService.getAllVehicles(PageRequest.of(pageNo-1, pageSize));
      ApiResponse<List<Vehicle>> response = new ApiResponse<>(true, "Vehicles fetched successfully", vehicles, null);
      return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<Vehicle>> getVehicleById(@PathVariable int id) {
      Vehicle vehicle = vehicleService.getVehicleById(id);
      ApiResponse<Vehicle> response = new ApiResponse<>(true, "Vehicle fetched successfully", vehicle, null);
      return ResponseEntity.ok(response);
  }
}
