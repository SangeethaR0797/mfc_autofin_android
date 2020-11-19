package model.basic_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanDetails
{

        @SerializedName("RequiredLoanAmount")
        @Expose
        private String requiredLoanAmount;
        @SerializedName("NoOfExistingLoans")
        @Expose
        private String noOfExistingLoans;
        @SerializedName("LoanTenure")
        @Expose
        private Integer loanTenure;

        public String getRequiredLoanAmount() {
            return requiredLoanAmount;
        }

        public void setRequiredLoanAmount(String requiredLoanAmount) {
            this.requiredLoanAmount = requiredLoanAmount;
        }

        public String getNoOfExistingLoans() {
            return noOfExistingLoans;
        }

        public void setNoOfExistingLoans(String noOfExistingLoans) {
            this.noOfExistingLoans = noOfExistingLoans;
        }

        public Integer getLoanTenure() {
            return loanTenure;
        }

        public void setLoanTenure(Integer loanTenure) {
            this.loanTenure = loanTenure;
        }

}
