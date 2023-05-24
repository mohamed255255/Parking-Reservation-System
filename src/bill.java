import java.time.LocalTime;
import java.util.ArrayList;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.lang.Math;
public class bill {
    public double price = 0 ;
    public int vehicleNumber ;
    public LocalTime st_time;
    public LocalTime end_time;
    public static ArrayList<bill> bills_list = new ArrayList<bill>(); /// boolean arr to check the empty slots

    public bill(){}


public double incomeVehicle(LocalTime start_date, LocalTime end_date) {
    end_date = LocalTime.now();
    long min = MINUTES.between(start_date, end_date);
    double hrs = Math.ceil(min/60.0);
        return hrs * 5;
}

    public bill(Reservation obj){
        st_time = obj.st_time;
        end_time = obj.end_time;
        vehicleNumber = obj.vehicleNumber;
        price =  incomeVehicle(st_time , end_time);
    }

}
