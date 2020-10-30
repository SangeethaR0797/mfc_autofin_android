package model.ibb_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IBBVehDetailsReq {
    @SerializedName("for")
    @Expose
    private String _for;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("year")
    @Expose
    private String year;

    public IBBVehDetailsReq(String _for, String accessToken, String tag) {
        this._for = _for;
        this.accessToken = accessToken;
        this.tag = tag;
    }

    public IBBVehDetailsReq(String _for, String accessToken, String tag, String year) {
        this._for = _for;
        this.accessToken = accessToken;
        this.tag = tag;
        this.year = year;
    }

    public IBBVehDetailsReq(String _for, String accessToken, String tag, String year, String month) {
        this._for = _for;
        this.accessToken = accessToken;
        this.month = month;
        this.tag = tag;
        this.year = year;
    }

    public IBBVehDetailsReq(String _for, String accessToken, String tag, String year, String month, String make) {
        this._for = _for;
        this.accessToken = accessToken;
        this.make = make;
        this.month = month;
        this.tag = tag;
        this.year = year;
    }

    public IBBVehDetailsReq(String _for, String accessToken, String tag, String year, String month, String make, String model) {
        this._for = _for;
        this.accessToken = accessToken;
        this.make = make;
        this.model = model;
        this.month = month;
        this.tag = tag;
        this.year = year;
    }

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
