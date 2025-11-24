package com.garage_system.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.SlotDto;
import com.garage_system.Model.Garage;
import com.garage_system.Model.Slot;
import com.garage_system.Repository.GarageRepository;
import com.garage_system.Repository.SlotRepository;
import com.garage_system.exception.ResourceNotFoundException;
import com.garage_system.mapper.SlotMapper;

@Service
public class SlotService {
    
    private final SlotRepository slotRepository;
    private final GarageRepository garageRepository ;

    public SlotService(SlotRepository slotRepository , GarageRepository garageRepository) {
        this.slotRepository   = slotRepository ;
        this.garageRepository = garageRepository ;
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
}
