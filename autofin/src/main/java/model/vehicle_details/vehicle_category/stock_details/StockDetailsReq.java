package model.vehicle_details.vehicle_category.stock_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockDetailsReq {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("Data")
    @Expose
    private StockData data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public StockData getData() {
        return data;
    }

    public void setData(StockData data) {
        this.data = data;
    }

}

