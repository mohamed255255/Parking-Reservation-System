import java.time.LocalTime;
public class FirstFit implements  configuration {
    public void parkin(vehicle obj) {
        boolean notEntered = true;
        for (int i = 0; i < garage.slotNum; i++) {
            if (garage.temp_slots.get(i).slotWidth >= obj.vehicleWidth && garage.temp_slots.get(i).slotDepth >= obj.vehicledepth && !garage.check.get(i)) {
                    garage.slots[i] = obj;
                    LocalTime t = LocalTime.now();
                    Reservation res = new Reservation(i + 1, obj.vehicleNumber, t);
                    garage.reservation_list[i] = res;
                    garage.check.add(i,true);
                    notEntered = false;
                    break;
                }
            }
            if (notEntered) System.out.println("there is no slot available for this vehicle");

    }
}