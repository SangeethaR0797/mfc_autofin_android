package model.add_lead_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddLeadRequest {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("Data")
    @Expose
    private AddLeadDetails data;

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

    public AddLeadDetails getData() {
        return data;
    }

    public void setData(AddLeadDetails data) {
        this.data = data;
    }


}
