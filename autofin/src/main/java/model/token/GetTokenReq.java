package model.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTokenReq {

    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("RequestFrom")
    @Expose
    private String requestFrom;
    @SerializedName("Data")
    @Expose
    private String data;

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

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
