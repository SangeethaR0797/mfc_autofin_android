package model.basic_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicData
{
        @SerializedName("CustomerId")
        @Expose
        private Integer customerId;
        @SerializedName("residentialDetails")
        @Expose
        private ResidentialDetails residentialDetails;
        @SerializedName("personalDetails")
        @Expose
        private PersonalDetailsData personalDetails;
        @SerializedName("loanDetails")
        @Expose
        private LoanDetails loanDetails;
        @SerializedName("employmentDetails")
        @Expose
        private EmploymentDetails employmentDetails;
        @SerializedName("vehicleDetails")
        @Expose
        private BasicVehDetails vehicleDetails;
        @SerializedName("referenceDetails")
        @Expose
        private ReferenceDetails referenceDetails;

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public ResidentialDetails getResidentialDetails() {
            return residentialDetails;
        }

        public void setResidentialDetails(ResidentialDetails residentialDetails) {
            this.residentialDetails = residentialDetails;
        }

        public PersonalDetailsData getPersonalDetails() {
            return personalDetails;
        }

        public void setPersonalDetails(PersonalDetailsData personalDetails) {
            this.personalDetails = personalDetails;
        }

        public LoanDetails getLoanDetails() {
            return loanDetails;
        }

        public void setLoanDetails(LoanDetails loanDetails) {
            this.loanDetails = loanDetails;
        }

        public EmploymentDetails getEmploymentDetails() {
            return employmentDetails;
        }

        public void setEmploymentDetails(EmploymentDetails employmentDetails) {
            this.employmentDetails = employmentDetails;
        }

        public BasicVehDetails getVehicleDetails() {
            return vehicleDetails;
        }

        public void setVehicleDetails(BasicVehDetails vehicleDetails) {
            this.vehicleDetails = vehicleDetails;
        }

        public ReferenceDetails getReferenceDetails() {
            return referenceDetails;
        }

        public void setReferenceDetails(ReferenceDetails referenceDetails) {
            this.referenceDetails = referenceDetails;
        }

}
