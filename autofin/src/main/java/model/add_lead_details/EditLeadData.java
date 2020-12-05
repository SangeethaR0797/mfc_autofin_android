package model.add_lead_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import model.basic_details.BasicDetails;
import model.vehicle_details.vehicle_category.VehicleDetails;

public class EditLeadData
{
        @SerializedName("CustomerId")
        @Expose
        private Integer customerId;
        @SerializedName("loanDetails")
        @Expose
        private LoanDetails loanDetails;
        @SerializedName("vehicleDetails")
        @Expose
        private VehicleDetails vehicleDetails;
        @SerializedName("basicDetails")
        @Expose
        private BasicDetails basicDetails;

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public LoanDetails getLoanDetails() {
            return loanDetails;
        }

        public void setLoanDetails(LoanDetails loanDetails) {
            this.loanDetails = loanDetails;
        }

        public VehicleDetails getVehicleDetails() {
            return vehicleDetails;
        }

        public void setVehicleDetails(VehicleDetails vehicleDetails) {
            this.vehicleDetails = vehicleDetails;
        }

        public BasicDetails getBasicDetails() {
            return basicDetails;
        }

        public void setBasicDetails(BasicDetails basicDetails) {
            this.basicDetails = basicDetails;
        }

}
