package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerData {

    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("caseId")
    @Expose
    private String caseId;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("variant")
    @Expose
    private String variant;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mainStatus")
    @Expose
    private String mainStatus;
    @SerializedName("bankStatus")
    @Expose
    private String bankStatus;
    @SerializedName("rtoStatus")
    @Expose
    private String rtoStatus;
    @SerializedName("rtoComment")
    @Expose
    private Object rtoComment;
    @SerializedName("assignedBank")
    @Expose
    private Object assignedBank;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMainStatus() {
        return mainStatus;
    }

    public void setMainStatus(String mainStatus) {
        this.mainStatus = mainStatus;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getRtoStatus() {
        return rtoStatus;
    }

    public void setRtoStatus(String rtoStatus) {
        this.rtoStatus = rtoStatus;
    }

    public Object getRtoComment() {
        return rtoComment;
    }

    public void setRtoComment(Object rtoComment) {
        this.rtoComment = rtoComment;
    }

    public Object getAssignedBank() {
        return assignedBank;
    }

    public void setAssignedBank(Object assignedBank) {
        this.assignedBank = assignedBank;
    }

}
