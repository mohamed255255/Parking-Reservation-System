package com.garage_system.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.DTO.request.SlotDto;
import com.garage_system.DTO.request.VehicleDto;
import com.garage_system.Model.Slot;
import com.garage_system.Model.Vehicle;
import com.garage_system.Service.SlotService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/slots")
@Tag(name = "Slot", description = "Slot CRUD for Admin")
public class SlotController {
    
    private final SlotService slotService;

    @Autowired
    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    public ResponseEntity<String> addNewSlot(@RequestBody SlotDto slotDto) {
        slotService.addNewSlot(slotDto);
        return ResponseEntity.ok("Slot is added successfully");
    }

    @GetMapping
    public List<Slot> getAllSlots() {
        return slotService.getAllSlots();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Slot> getSlotById(@PathVariable("id") int id) {
        Slot slot = slotService.getSlotById(id);
        return ResponseEntity.ok(slot);
    }

     // add Vehicle to fitting-empty slot
     @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
     @PostMapping("/{slotId}/vehicle")
     public ResponseEntity<?> addVehicleToAnEmptySlot(@PathVariable("slotId") int slotId , @RequestBody VehicleDto vehicleDto) {
        
         var mp    =  slotService.addVehicleToAnEmptySlot(slotId , vehicleDto);
         var entry = mp.entrySet().iterator().next();
        
         int status_code = (Integer) entry.getKey() ;
         String message  = entry.getValue().toString();
       
         return ResponseEntity.status(status_code).body(Map.of("message" , message));
    }

}
