package com.garage_system.Service;

import com.garage_system.Model.Reservation;
import com.garage_system.Model.Garage;
import com.garage_system.Model.Vehicle;

import java.time.LocalTime;
public class BestFit implements ParkingStrategy{
        @Override  
    public void parkin(Vehicle obj) {
        // TODO 
    
    }
}
    /*
    public static void parkin(vehicle obj){
        boolean notEntered = true ;
        //// minimum dimentsions
        main.sort(garage.slotNum, garage.arr); //// sort arr of pair ascendingly from minimum com.garage_system.Model.slot to maximum in area
        for (int i = 0; i < garage.slotNum; i++) {
            if (garage.arr[i].x.slotWidth >= obj.vehicleWidth && garage.arr[i].x.slotDepth >= obj.vehicledepth && !garage.check.get(i)) {
                int indx = garage.arr[i].y;
                garage.slots[indx] = obj;
                garage.check.add(i,true);
                LocalTime t = LocalTime.now();
                Reservation res = new Reservation(i + 1, obj.vehicleNumber, t); //// gather info of that car
                garage.reservation_list[i] = res; /// store the infro in reservation_list where i can get anyinfo about each car in my com.garage_system.Model.garage
                //// keep in mind that com.garage_system.Model.garage slots are parallel to reservation_list indces in both arrays
                notEntered = false;
                break;
            }
        }
        if (notEntered) System.out.println("there is no com.garage_system.Model.slot available for this com.garage_system.Model.vehicle");
    }

*/
}
