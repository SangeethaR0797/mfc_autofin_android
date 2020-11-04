package model.vehicle_details.vehicle_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VehicleDetails {

    @SerializedName("haveVehicleNumber")
    @Expose
    private Boolean haveVehicleNumber;
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
    private Boolean doesCarHaveLoan;
    @SerializedName("valuationPrice")
    @Expose
    private String valuationPrice;
    @SerializedName("valuationreport")
    @Expose
    private String valuationreport;
    @SerializedName("insurance")
    @Expose
    private Boolean insurance;
    @SerializedName("insuranceAmount")
    @Expose
    private String insuranceAmount;
    @SerializedName("insuranceValidity")
    @Expose
    private String insuranceValidity;
    @SerializedName("insuranceType")
    @Expose
    private String insuranceType;
    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;

    public Boolean getHaveVehicleNumber() {
        return haveVehicleNumber;
    }

    public void setHaveVehicleNumber(Boolean haveVehicleNumber) {
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

    public Boolean getDoesCarHaveLoan() {
        return doesCarHaveLoan;
    }

    public void setDoesCarHaveLoan(Boolean doesCarHaveLoan) {
        this.doesCarHaveLoan = doesCarHaveLoan;
    }

    public String getValuationPrice() {
        return valuationPrice;
    }

    public void setValuationPrice(String valuationPrice) {
        this.valuationPrice = valuationPrice;
    }

    public String getValuationreport() {
        return valuationreport;
    }

    public void setValuationreport(String valuationreport) {
        this.valuationreport = valuationreport;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(String insuranceAmount) {
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

}
