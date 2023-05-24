import java.time.LocalTime;
import java.util.ArrayList;

public class garage {
    public static int slotNum ; ///
    public static ArrayList<slot> temp_slots = new ArrayList<slot>(); //// this array takes slot objects with specified dimensions based on what the user put
    public static vehicle slots[] = new vehicle[100];//// this array stores vehicles if a vehicle meet the required dimensions in temp array
    public static Reservation reservation_list[] = new Reservation[100]; /// to store res info about car including st_time , reserved slot etc ;
    public static ArrayList<Boolean> check = new ArrayList<Boolean>(); /// boolean arr to check the empty slots
    static main.Pair arr[] = new main.Pair[10000];
    public static void store_arr_of_pair(main.Pair arr[], ArrayList<slot> temp_slots) { /// fun to set every index with index to save the index of the original slot  in sort function
             for (int i = 0; i < slotNum; i++) {
                    main.Pair p = new main().new Pair(temp_slots.get(i) , i ); /// decalre pair
                    arr[i] = p; /// add the pair
        }
    }
    public static void initalize(){
        for (int i = 0; i < slotNum; i++) {
            check.add(i,false);
        }
    }



    public  void setslotnum(int slotNum) {
        this.slotNum = slotNum;
    }
    public  int getSlot_num() {
        return slotNum;
    }
    public  void intialize_slot() {
        for (int i = 0; i < slotNum; i++) {
            slots[i] = new vehicle(0, 0, "THIS SLOT IS EMPTY", 0, 0);
        }
    }


    public void display_available_slots() {
        System.out.println("available slots are : ");
        boolean enter = false;
        for (int i = 0; i < slotNum; i++) {
            if (!check.get(i)){
                System.out.print((i + 1) + " ");
                enter = true ;
            }
        }
        if(!enter){
            System.out.println("none , the garage is full");
        }
    }

    public void park_out(int n) {
       for(int i = 0 ; i < slotNum ; i++){
           if(n == slots[i].vehicleNumber){
               check.add(i , false) ;
               garage.reservation_list[i].end_time = LocalTime.now();
               bill.bills_list.add( new bill(reservation_list[i]) );
               slots[i] = new vehicle(0, 0, "THIS SLOT IS EMPTY", 0, 0) ;
               bill b = new bill();
               System.out.println(b.incomeVehicle(reservation_list[i].st_time ,LocalTime.now()));
           }
       }
}

    /*
    public   void display() {
        for (int i = 0; i < slotNum; i++) {
            System.out.println(slots[i].modelname);
        }
    }*/



}
