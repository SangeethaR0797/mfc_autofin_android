package model.vehicle_details.vehicle_category.stock_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockData {

    @SerializedName("VehicleNumber")
    @Expose
    private String vehicleNumber;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

}
