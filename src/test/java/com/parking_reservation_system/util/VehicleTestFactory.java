package com.parking_reservation_system.util;

import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.model.VehicleType;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class VehicleTestFactory {
    private VehicleTestFactory(){}
    
    public static Vehicle createTestVehicleForUser(User user) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber("PLATE-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        vehicle.setModelYear(2015 + random.nextInt(10));
        vehicle.setModelName("Model-" + random.nextInt(50));
        vehicle.setVehicleWidth(475); 
        vehicle.setVehicleDepth(176);
        vehicle.setType(VehicleType.values()[random.nextInt(VehicleType.values().length)]);
        vehicle.setUser(user);
        return vehicle;
    }
}