package model.bank_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SelectRecBankReq {

    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("Data")
    @Expose
    private SelectedBankData data;

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

    public SelectedBankData getData() {
        return data;
    }

    public void setData(SelectedBankData data) {
        this.data = data;
    }

}
