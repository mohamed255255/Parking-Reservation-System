public class vehicle {
    public int vehicleNumber;
    public int modelYear;
    public String modelname;
    public double vehicleWidth;
    public double vehicledepth;

    public vehicle(int vehicleNumber, int modelYear, String modelname, double vehicleWidth, double vehicledepth) { //// constructor
        this.vehicleNumber = vehicleNumber;
        this.modelname = modelname;
        this.vehicleWidth = vehicleWidth;
        this.vehicledepth = vehicledepth;
        this.modelYear = modelYear;
    }

    public int getVehicleNumber() {
        return vehicleNumber;
    }

    public double getVehicledepth() {
        return vehicledepth;
    }

    public double getVehicleWidth() {
        return vehicleWidth;
    }
}
