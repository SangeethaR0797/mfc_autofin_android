package model.bank_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterestedBankOfferResData
{

        @SerializedName("customerId")
        @Expose
        private Integer customerId;
        @SerializedName("caseId")
        @Expose
        private String caseId;
        @SerializedName("bankName")
        @Expose
        private String bankName;
        @SerializedName("loanAmount")
        @Expose
        private String loanAmount;
        @SerializedName("rateOfIntrest")
        @Expose
        private String rateOfIntrest;
        @SerializedName("tenure")
        @Expose
        private String tenure;
        @SerializedName("emi")
        @Expose
        private String emi;
        @SerializedName("selected")
        @Expose
        private Boolean selected;
        @SerializedName("recommendedBankId")
        @Expose
        private Object recommendedBankId;
        @SerializedName("comeFrom")
        @Expose
        private Object comeFrom;

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

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            this.loanAmount = loanAmount;
        }

        public String getRateOfIntrest() {
            return rateOfIntrest;
        }

        public void setRateOfIntrest(String rateOfIntrest) {
            this.rateOfIntrest = rateOfIntrest;
        }

        public String getTenure() {
            return tenure;
        }

        public void setTenure(String tenure) {
            this.tenure = tenure;
        }

        public String getEmi() {
            return emi;
        }

        public void setEmi(String emi) {
            this.emi = emi;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        public Object getRecommendedBankId() {
            return recommendedBankId;
        }

        public void setRecommendedBankId(Object recommendedBankId) {
            this.recommendedBankId = recommendedBankId;
        }

        public Object getComeFrom() {
            return comeFrom;
        }

        public void setComeFrom(Object comeFrom) {
            this.comeFrom = comeFrom;
        }

}
