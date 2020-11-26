package model.addtional_fields;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAdditionFieldsReq {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("Data")
    @Expose
    private BankName data;

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

    public BankName getData() {
        return data;
    }

    public void setData(BankName data) {
        this.data = data;
    }

}
