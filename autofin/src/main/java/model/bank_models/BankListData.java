package model.bank_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankListData
{

        @SerializedName("bankId")
        @Expose
        private Integer bankId;
        @SerializedName("bankName")
        @Expose
        private String bankName;
        @SerializedName("loanAmount")
        @Expose
        private String loanAmount;
        @SerializedName("roi")
        @Expose
        private String roi;
        @SerializedName("emi")
        @Expose
        private String emi;
        @SerializedName("tenure")
        @Expose
        private String tenure;

        public Integer getBankId() {
            return bankId;
        }

        public void setBankId(Integer bankId) {
            this.bankId = bankId;
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

        public String getRoi() {
            return roi;
        }

        public void setRoi(String roi) {
            this.roi = roi;
        }

        public String getEmi() {
            return emi;
        }

        public void setEmi(String emi) {
            this.emi = emi;
        }

        public String getTenure() {
            return tenure;
        }

        public void setTenure(String tenure) {
            this.tenure = tenure;
        }


}
