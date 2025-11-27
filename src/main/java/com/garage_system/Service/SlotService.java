package com.garage_system.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.garage_system.DTO.request.SlotDto;
import com.garage_system.DTO.request.VehicleDto;
import com.garage_system.Model.Garage;
import com.garage_system.Model.Slot;
import com.garage_system.Model.Vehicle;
import com.garage_system.Repository.GarageRepository;
import com.garage_system.Repository.SlotRepository;
import com.garage_system.Repository.VehicleRepository;
import com.garage_system.exception.ResourceNotFoundException;
import com.garage_system.mapper.SlotMapper;
import com.garage_system.mapper.VehicleMapper;

@Service
public class SlotService {
    
    private final SlotRepository slotRepository;
    private final GarageRepository garageRepository ;
    private final VehicleRepository vehicleRepository ;

    public SlotService(SlotRepository slotRepository , GarageRepository garageRepository , VehicleRepository vehicleRepository) {
        this.slotRepository    = slotRepository ;
        this.garageRepository  = garageRepository ;
        this.vehicleRepository = vehicleRepository ;
    }

    public void addNewSlot(SlotDto slotDto) {
        Slot   newSlot         = SlotMapper.toEntity(slotDto) ;
        Garage existedGarage = garageRepository.findById(slotDto.getGarage_id())
        .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + slotDto.getGarage_id()));
      
        newSlot.setGarage(existedGarage);
        slotRepository.save(newSlot);       
    }

    public List<Slot> getAllSlots(){
        return slotRepository.getAllSlots();
    }
     
    public Slot getSlotById(int id){
        return slotRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("slot not found with id: "+id)) ;
    }

    public Map<?,?> addVehicleToAnEmptySlot(int slotId , VehicleDto vehicleDto){
        Slot slot = slotRepository.findById(slotId).get();
        boolean isEmpty = slot.getVehicle() == null;
        if(isEmpty){
             if(vehicleDto.getVehicleDepth()<=slot.getSlotDepth()&&vehicleDto.getVehicleWidth()<=slot.getSlotWidth()){
                   
                Vehicle myVehicle = vehicleRepository.findById(vehicleDto.getId())
                      .orElseThrow(() -> new ResourceNotFoundException("Vehicle is not found"));
                   
                     slot.setVehicle(myVehicle);
                     slotRepository.save(slot);
                    
                     return Map.of( 201 , "vehicle is added successfully to slot number " + slotId );
             }else{
                   return Map.of(400 , "the vehicle dimensions don't fit properly")   ;
             }
        }
          return Map.of(409 , "the slot number " + slotId + " is busy")   ;
    }





}
