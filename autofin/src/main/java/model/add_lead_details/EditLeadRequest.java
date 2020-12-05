package model.add_lead_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EditLeadRequest {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("Data")
    @Expose
    private EditLeadData data;

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

    public EditLeadData getData() {
        return data;
    }

    public void setData(EditLeadData data) {
        this.data = data;
    }
}
