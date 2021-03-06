package model.vehicle_details.vehicle_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VehicleDetails {

    @SerializedName("haveVehicleNumber")
    @Expose
    private boolean haveVehicleNumber;
    @SerializedName("registrationYear")
    @Expose
    private String registrationYear;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("variant")
    @Expose
    private String variant;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("ownership")
    @Expose
    private Integer ownership;
    @SerializedName("doesCarHaveLoan")
    @Expose
    private boolean doesCarHaveLoan;
    @SerializedName("valuationPrice")
    @Expose
    private double valuationPrice;
    @SerializedName("insurance")
    @Expose
    private boolean insurance;
    @SerializedName("insuranceAmount")
    @Expose
    private double insuranceAmount;
    @SerializedName("insuranceValidity")
    @Expose
    private String insuranceValidity;
    @SerializedName("insuranceType")
    @Expose
    private String insuranceType;
    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;
    @SerializedName("onRoadPrice")
    @Expose
    private double onRoadPrice;
    @SerializedName("isValuationDone")
    @Expose
    private boolean isValuationDone;
    @SerializedName("vehicleSellingPrice")
    @Expose
    private double vehicleSellingPrice;
    @SerializedName("likelyPurchaseDate")
    @Expose
    private String likelyPurchaseDate;

    public String getLikelyPurchaseDate() {
        return likelyPurchaseDate;
    }

    public void setLikelyPurchaseDate(String likelyPurchaseDate) {
        this.likelyPurchaseDate = likelyPurchaseDate;
    }

    public boolean getHaveVehicleNumber() {
        return haveVehicleNumber;
    }

    public void setHaveVehicleNumber(boolean haveVehicleNumber) {
        this.haveVehicleNumber = haveVehicleNumber;
    }

    public String getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(String registrationYear) {
        this.registrationYear = registrationYear;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getOwnership() {
        return ownership;
    }

    public void setOwnership(Integer ownership) {
        this.ownership = ownership;
    }

    public boolean getDoesCarHaveLoan() {
        return doesCarHaveLoan;
    }

    public void setDoesCarHaveLoan(boolean doesCarHaveLoan) {
        this.doesCarHaveLoan = doesCarHaveLoan;
    }

    public double getValuationPrice() {
        return valuationPrice;
    }

    public void setValuationPrice(double valuationPrice) {
        this.valuationPrice = valuationPrice;
    }

    public boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public double getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(double insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getInsuranceValidity() {
        return insuranceValidity;
    }

    public void setInsuranceValidity(String insuranceValidity) {
        this.insuranceValidity = insuranceValidity;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public double getOnRoadPrice() {
        return onRoadPrice;
    }

    public void setOnRoadPrice(double onRoadPrice) {
        this.onRoadPrice = onRoadPrice;
    }

    public boolean getIsValuationDone() {
        return isValuationDone;
    }

    public void setIsValuationDone(boolean isValuationDone) {
        this.isValuationDone = isValuationDone;
    }

    public double getVehicleSellingPrice() {
        return vehicleSellingPrice;
    }

    public void setVehicleSellingPrice(double vehicleSellingPrice) {
        this.vehicleSellingPrice = vehicleSellingPrice;
    }
}
