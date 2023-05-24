import java.time.LocalTime;
public class Reservation {
        public LocalTime st_time;
        public LocalTime end_time;
        public  int slotnumber;
        public int vehicleNumber;
        public Reservation() {}
        public Reservation(int slotnumber, int vehicleNumber, LocalTime t) {
            this.slotnumber = slotnumber;
            this.vehicleNumber = vehicleNumber;
            st_time = t;
        }


   }


