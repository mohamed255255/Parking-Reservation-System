package com.parking_reservation_system.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.parking_reservation_system.dto.request.SlotDto;
import com.parking_reservation_system.dto.response.SlotResponseDto;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.SlotMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.GarageRepository;
import com.parking_reservation_system.repository.SlotRepository;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SlotService {
    
    private final SlotRepository slotRepository;
    private final GarageRepository garageRepository ;
    private final VehicleRepository vehicleRepository ;
    private final QRCodeService qrCodeService ;

 /// admin
    public SlotResponseDto createSlot(SlotDto slotDto)  throws IOException , WriterException{

        Garage existedGarage   = garageRepository.findById(slotDto.garage_id())
        .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + slotDto.garage_id()));
       
        Slot   newSlot         = SlotMapper.toEntity(slotDto) ;
        newSlot.setGarage(existedGarage);

        String qrCodePath = qrCodeService.saveQRCodeImage(slotDto) ;
        newSlot.setQrCodeImagePath(qrCodePath);

        return SlotMapper.toResponseDto(slotRepository.save(newSlot));       
    }

///// to do :
    public List<SlotResponseDto> getUserSlots(){

         User currentAuthUser = ((CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal()).getUser();
        
        if(currentAuthUser == null) 
            throw new ResourceNotFoundException("User is not found") ;
       
        return slotRepository
        .getUserSlotsAndVehicles(currentAuthUser.getId())
        .stream()
        .map(slot -> SlotMapper.toResponseDto(slot))
        .toList();
        
    }

    public SlotResponseDto getSlotById(int id){
        return slotRepository.findById(id)
               .map(slot -> SlotMapper.toResponseDto(slot))
               .orElseThrow(()-> new ResourceNotFoundException("slot not found with id: "+id)) ;
    }


    public void addVehicleToAnEmptySlot(int slotId , int vehicleId){
        Slot slot = slotRepository.findById(slotId).get();
        Vehicle vehicle = vehicleRepository.findById(vehicleId).get() ;

        boolean isEmpty = slot.getVehicle() == null;
        if(isEmpty){
             if(vehicle.getVehicleDepth() <= slot.getSlotDepth() && vehicle.getVehicleWidth()<=slot.getSlotWidth()){
                   
                Vehicle myVehicle = vehicleRepository.findById(vehicle.getId())
                     .orElseThrow(() -> new ResourceNotFoundException("Vehicle is not found"));              
                     slot.setVehicle(myVehicle);
                     slotRepository.save(slot); 
                     return ; 
             }else{
                   throw new RuntimeException("the vehicle dimensions don't fit properly")   ;
             }
        }
         throw new RuntimeException("the slot number " + slotId + " is already busy") ;  
    }





}
