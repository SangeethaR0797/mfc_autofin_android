package model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetailsRes {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("data")
    @Expose
    private CountAndCustData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public CountAndCustData getData() {
        return data;
    }

    public void setData(CountAndCustData data) {
        this.data = data;
    }

}


