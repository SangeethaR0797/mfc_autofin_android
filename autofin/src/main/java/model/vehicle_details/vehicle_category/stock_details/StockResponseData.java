package model.vehicle_details.vehicle_category.stock_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockResponseData {

    @SerializedName("stockId")
    @Expose
    private Integer stockId;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("variant")
    @Expose
    private String variant;
    @SerializedName("fuelType")
    @Expose
    private String fuelType;
    @SerializedName("insurance")
    @Expose
    private String insurance;
    @SerializedName("insuranceType")
    @Expose
    private String insuranceType;
    @SerializedName("insuranceValidity")
    @Expose
    private Object insuranceValidity;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("ibbMake")
    @Expose
    private String ibbMake;
    @SerializedName("ibbModel")
    @Expose
    private String ibbModel;
    @SerializedName("ibbVariant")
    @Expose
    private String ibbVariant;

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
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

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public Object getInsuranceValidity() {
        return insuranceValidity;
    }

    public void setInsuranceValidity(Object insuranceValidity) {
        this.insuranceValidity = insuranceValidity;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIbbMake() {
        return ibbMake;
    }

    public void setIbbMake(String ibbMake) {
        this.ibbMake = ibbMake;
    }

    public String getIbbModel() {
        return ibbModel;
    }

    public void setIbbModel(String ibbModel) {
        this.ibbModel = ibbModel;
    }

    public String getIbbVariant() {
        return ibbVariant;
    }

    public void setIbbVariant(String ibbVariant) {
        this.ibbVariant = ibbVariant;
    }

}


