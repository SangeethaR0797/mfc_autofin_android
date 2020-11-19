package model.bank_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SelectedBankData {
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("CaseId")
    @Expose
    private String caseId;
    @SerializedName("RecommendedBankId")
    @Expose
    private String recommendedBankId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getRecommendedBankId() {
        return recommendedBankId;
    }

    public void setRecommendedBankId(String recommendedBankId) {
        this.recommendedBankId = recommendedBankId;
    }

}
