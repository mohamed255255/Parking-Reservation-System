package com.parking_reservation_system.util;

import com.parking_reservation_system.dto.request.SlotDto;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.Vehicle;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SlotTestFactory {

    public static Slot createEmptySlot(Garage garage) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        Slot slot = new Slot();
        slot.setSlotNumber(random.nextInt(1, 500));
        slot.setSlotWidth(2.2 + (random.nextDouble() * 0.8));
        slot.setSlotDepth(5.0 + (random.nextDouble() * 2.0));
        slot.setQrCodeImagePath("/images/qrcodes/slot_" + uuid + ".png");
        slot.setGarage(garage != null ? garage : new Garage());
        return slot;
    }

    public static Slot createOccupiedSlot(Garage garage, Vehicle vehicle) {
        Slot slot = createEmptySlot(garage);
        slot.setVehicle(vehicle);
        return slot;
    }

    public static SlotDto createSlotDto(Garage garage) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
                return new SlotDto(
                    random.nextInt(1, 500),
                    random.nextInt(2, 5),  
                    random.nextInt(5, 10), 
                    garage.getId(),
                    null
                );    
     }

}