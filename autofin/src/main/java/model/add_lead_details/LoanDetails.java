package model.add_lead_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class LoanDetails {

        @SerializedName("loanCategory")
        @Expose
        private String loanCategory;
        @SerializedName("requiredLoanAmount")
        @Expose
        private double requiredLoanAmount;
        @SerializedName("noOfExistingLoans")
        @Expose
        private String noOfExistingLoans;
        @SerializedName("loanTenure")
        @Expose
        private String loanTenure;

        public String getLoanCategory() {
            return loanCategory;
        }

        public void setLoanCategory(String loanCategory) {
            this.loanCategory = loanCategory;
        }

        public double getRequiredLoanAmount() {
            return requiredLoanAmount;
        }

        public void setRequiredLoanAmount(double requiredLoanAmount) {
            this.requiredLoanAmount = requiredLoanAmount;
        }

        public String getNoOfExistingLoans() {
            return noOfExistingLoans;
        }

        public void setNoOfExistingLoans(String noOfExistingLoans) {
            this.noOfExistingLoans = noOfExistingLoans;
        }

        public String getLoanTenure() {
            return loanTenure;
        }

        public void setLoanTenure(String loanTenure) {
            this.loanTenure = loanTenure;
        }

}

