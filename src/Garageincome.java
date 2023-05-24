public class Garageincome {
    public double totalincome = 0 ;
    public double getTotalincome() {
        return totalincome;
    }
    public double  Totalincome(){
        for(int i = 0 ; i < bill.bills_list.size() ; i++){
            totalincome += bill.bills_list.get(i).price ;
        }
        return totalincome ;
    }


}
