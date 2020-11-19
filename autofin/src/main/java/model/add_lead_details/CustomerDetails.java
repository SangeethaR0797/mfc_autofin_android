package model.add_lead_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import model.basic_details.BasicDetails;
import model.basic_details.EmploymentDetails;
import model.basic_details.PersonalDetailsData;
import model.basic_details.ReferenceDetails;
import model.basic_details.ResidentialDetails;
import model.vehicle_details.vehicle_category.VehicleDetails;

public class CustomerDetails
{
        @SerializedName("customerId")
        @Expose
        private Integer customerId;
        @SerializedName("caseId")
        @Expose
        private String caseId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("basicDetails")
        @Expose
        private BasicDetails basicDetails;
        @SerializedName("vehicleDetails")
        @Expose
        private VehicleDetails vehicleDetails;
        @SerializedName("employmentDetails")
        @Expose
        private EmploymentDetails employmentDetails;
        @SerializedName("personalDetails")
        @Expose
        private PersonalDetailsData personalDetails;
        @SerializedName("loanDetails")
        @Expose
        private LoanDetails loanDetails;
        @SerializedName("residentialDetails")
        @Expose
        private ResidentialDetails residentialDetails;
        @SerializedName("referenceDetails")
        @Expose
        private ReferenceDetails referenceDetails;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BasicDetails getBasicDetails() {
            return basicDetails;
        }

        public void setBasicDetails(BasicDetails basicDetails) {
            this.basicDetails = basicDetails;
        }

        public VehicleDetails getVehicleDetails() {
            return vehicleDetails;
        }

        public void setVehicleDetails(VehicleDetails vehicleDetails) {
            this.vehicleDetails = vehicleDetails;
        }

        public EmploymentDetails getEmploymentDetails() {
            return employmentDetails;
        }

        public void setEmploymentDetails(EmploymentDetails employmentDetails) {
            this.employmentDetails = employmentDetails;
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

        public ResidentialDetails getResidentialDetails() {
            return residentialDetails;
        }

        public void setResidentialDetails(ResidentialDetails residentialDetails) {
            this.residentialDetails = residentialDetails;
        }

        public ReferenceDetails getReferenceDetails() {
            return referenceDetails;
        }

        public void setReferenceDetails(ReferenceDetails referenceDetails) {
            this.referenceDetails = referenceDetails;
        }

}
