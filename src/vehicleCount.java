public class vehicleCount {
    public static int TotalVehicleNumber = 0 ;
    public static int CurrentVehicleNumber = 0 ;
    public static int getTotalVehicleNumber() {
        return TotalVehicleNumber;
    }

    public static  int calc_current() {
        for(int i = 0 ; i < garage.slotNum;  i++){
            if(garage.check.get(i)) {
                CurrentVehicleNumber++;
            }
        }
        return CurrentVehicleNumber ;
    }


}
