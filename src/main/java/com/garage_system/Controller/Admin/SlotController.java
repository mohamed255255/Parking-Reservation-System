package com.garage_system.Controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import  org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.Model.Slot;
import com.garage_system.Service.Admin.SlotService;




@RestController
@RequestMapping("/slots")
public class SlotController {
    
    private final SlotService slotService ;

    @Autowired
    public SlotController(SlotService slotService){
          this.slotService = slotService ;
    }

    @PostMapping
    public ResponseEntity<String> addNewSlot(@RequestBody Slot slot) {
        this.slotService.addNewSlot(slot);
        return ResponseEntity.ok("slot is added successfuly") ;
    }
       
    @GetMapping
    public List<Slot> getAllSlots() {
        return this.slotService.getAllSlots();
    }
    

   @PutMapping("{id}")
   public String putMethodName(@PathVariable String id, @RequestBody String entity) {
       
       return entity;
   }




}
