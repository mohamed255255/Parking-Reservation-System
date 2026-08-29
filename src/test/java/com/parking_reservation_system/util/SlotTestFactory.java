package com.parking_reservation_system.util;

import com.parking_reservation_system.dto.request.SlotRequest;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.Vehicle;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SlotTestFactory {

    private SlotTestFactory() {}

    public static Slot createEmptySmallSlot(Garage garage) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        Slot slot = new Slot();
        slot.setSlotNumber(random.nextInt(1, 500));
        slot.setSlotWidth(10);
        slot.setSlotDepth(20);
        slot.setQrCodeImagePath("/images/qrcodes/slot_" + uuid + ".png");
        slot.setGarage(garage != null ? garage : new Garage());
        return slot;
    }

    public static Slot createEmptyLargeSlot(Garage garage) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        Slot slot = new Slot();
        slot.setSlotNumber(random.nextInt(1, 500));
        slot.setSlotWidth(600);
        slot.setSlotDepth(300);
        slot.setQrCodeImagePath("/images/qrcodes/slot_" + uuid + ".png");
        slot.setGarage(garage != null ? garage : new Garage());
        return slot;
    }

    public static Slot createOccupiedSlot(Garage garage, Vehicle vehicle) {
        Slot slot = createEmptyLargeSlot(garage);
        slot.setVehicle(vehicle);
        return slot;
    }

    public static SlotRequest createSlotRequest(Garage garage) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new SlotRequest(
                random.nextInt(1, 500),
                random.nextInt(2, 5),
                random.nextInt(5, 10),
                garage.getId(),
                null);
    }
}
