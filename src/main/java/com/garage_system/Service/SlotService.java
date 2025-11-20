package com.garage_system.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.SlotDto;
import com.garage_system.Model.Slot;
import com.garage_system.Repository.SlotRepository;
import com.garage_system.mapper.SlotMapper;

@Service
public class SlotService {
    
    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public void addNewSlot(SlotDto slotDto) {
        Slot newslot = SlotMapper.toEntity(slotDto) ;
        slotRepository.save(newslot);
    }

    public List<Slot> getAllSlots(){
        return slotRepository.getAllSlots();
    }
     
    public Slot getSlotById( int id){
        return slotRepository.findById(id).orElseThrow(()-> new RuntimeException("slot not found with id: "+id)) ;
    }
}
