package com.garage_system.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.garage_system.dto.request.SlotDto;
import com.garage_system.dto.request.VehicleDto;
import com.garage_system.exception.ResourceNotFoundException;
import com.garage_system.mapper.SlotMapper;
import com.garage_system.model.Garage;
import com.garage_system.model.Slot;
import com.garage_system.model.User;
import com.garage_system.model.Vehicle;
import com.garage_system.repository.GarageRepository;
import com.garage_system.repository.SlotRepository;
import com.garage_system.repository.VehicleRepository;
import com.garage_system.security.CustomUserDetails;
import com.google.zxing.WriterException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SlotService {
    
    private final SlotRepository slotRepository;
    private final GarageRepository garageRepository ;
    private final VehicleRepository vehicleRepository ;
    private final QRCodeService qrCodeService ;

 
    public void createSlot(SlotDto slotDto)  throws IOException , WriterException{

        Slot   newSlot         = SlotMapper.toEntity(slotDto) ;
        Garage existedGarage   = garageRepository.findById(slotDto.getGarage_id())
        .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + slotDto.getGarage_id()));
       
        newSlot.setGarage(existedGarage);
        String qrCodePath = qrCodeService.saveQRCodeImage(slotDto) ;
        newSlot.setQrCodeImagePath(qrCodePath);
        slotRepository.save(newSlot);       
    }

///// to do :
    public List<Slot> getUserSlots(){
        User currentAuthUser = ((CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal()).getUser();
        
        if(currentAuthUser == null) throw new ResourceNotFoundException("User is not found") ;
        return slotRepository.getUserSlotsAndVehicles(currentAuthUser.getId());
        
    }
    /// admin 
    public Slot getSlotById(int id){
        return slotRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("slot not found with id: "+id)) ;
    }


    public Map<?,?> addVehicleToAnEmptySlot(int slotId , Vehicle vehicle){
        Slot slot = slotRepository.findById(slotId).get();
        boolean isEmpty = slot.getVehicle() == null;
        if(isEmpty){
             if(vehicle.getVehicleDepth() <= slot.getSlotDepth() && vehicle.getVehicleWidth()<=slot.getSlotWidth()){
                   
                Vehicle myVehicle = vehicleRepository.findById(vehicle.getId())
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
