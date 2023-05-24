import java.util.Scanner;
public class main {
    public class Pair {
        slot x = new slot();
        int y;
        // Constructor
        public Pair( slot x , int y)
        {
            this.x = x;
            this.y = y;
        }
    }
      public static void fswap(Pair p1  , Pair p2) {
            slot temp = new slot();
            temp.slotWidth = p1.x.slotWidth;
            temp.slotDepth = p1.x.slotDepth;
            int tempnum = p1.y ;
            p1.x.slotWidth = p2.x.slotWidth;
            p1.x.slotDepth = p2.x.slotDepth;
            p1.y = p2.y;
            p2.x.slotWidth = temp.slotWidth;
            p2.x.slotDepth = temp.slotDepth;
            p2.y = tempnum;

        }
        public static void sort(int n, Pair arr[]) { /// bubble sort to apply 2nd config to build a garage like this  bike < car < van < truck  shape
            for (int i = 0; i < n - 1; i++)
                for (int j = 0; j < n - i - 1; j++){
                    if (arr[j].x.slotWidth > arr[j+1].x.slotWidth ) {
                        fswap(arr[j] , arr[j+1]);

                    } else if (arr[j].x.slotDepth > arr[j+1].x.slotDepth) {
                        fswap(arr[j] , arr[j+1]);
                    }
                }
        }
        public static void main(String[] args) {
            AdminAccount acc = new AdminAccount();
            acc.login();
            Garageincome gi = new Garageincome();
            if (acc.checkAccount()) {
                garage g = new garage(); /// initialize garage obj and work on it this object represent an actual garage
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter the maximum number of slots in the garage");
                int slotNum = sc.nextInt();
                g.setslotnum(slotNum);
                System.out.println("Enter the dimensions of each slot");

                for (int i = 0; i < g.getSlot_num(); i++) {
                    System.out.println("slot number " + (i + 1));
                    System.out.print("Width: ");
                    double w = sc.nextInt();
                    System.out.print("Depth: ");
                    double d = sc.nextInt();
                    slot s = new slot(w, d);
                    g.temp_slots.add(s);
                }
                g.intialize_slot(); //// to avoid null slot
               garage.initalize(); ///  to avoid null in arraylist

            g.store_arr_of_pair(g.arr , g.temp_slots );
                int choice;
                System.out.println("choose park-in algorithm :");
                System.out.println("1.Fisrt Fit");
                System.out.println("2.Best Fit");
                int in = sc.nextInt();
            for (int i = 0; i < g.slotNum; i++) {
                    System.out.println(" ---------------------------------------------");
                    System.out.println(" 1. add a vehicle");
                    System.out.println(" 2.park out by carID");
                    System.out.println(" 3.show available slots");
                    System.out.println(" 4. Exit");
                    choice = sc.nextInt();
                    if (choice == 1) {
                        vehicleCount.TotalVehicleNumber++;
                        FirstFit obj = new FirstFit();  ////
                        BestFit obj2 = new BestFit(); /////

                        System.out.println("your vehicle number:");
                        int num = sc.nextInt();
                        System.out.println("car model name:");
                        String modelname = sc.next();
                        System.out.println("model year:");
                        int y = sc.nextInt();
                        System.out.println("vehicle width:");
                        int wid = sc.nextInt();
                        System.out.println("vehicle depth:");
                        int dep = sc.nextInt();
                        vehicle v = new vehicle(num, y, modelname, wid, dep);
                        if(in == 1){
                            obj.parkin(v);
                        }else{
                            obj2.parkin(v);
                        }
                    }
                    else if(choice == 2){
                        System.out.println("put your car ID");
                        int n = sc.nextInt() ;
                        System.out.println("user needs to pay: ");
                        g.park_out(n);

                    }
                    else if(choice == 3){
                       g.display_available_slots();
                    }
                    else break; /// if u dont want to add more cars

             }
            g.display_available_slots();
            System.out.println("Total number of vehicles : "); //// including parkin and out
            System.out.println(vehicleCount.getTotalVehicleNumber());

            System.out.println("Total income of  the garage : "); //// including parkin and out
            System.out.println(gi.Totalincome());


            //g.display();
           }

       }

    };


