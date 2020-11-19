package model.basic_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResidentialDetails
{

        @SerializedName("Pincode")
        @Expose
        private String pincode;
        @SerializedName("CustomerCity")
        @Expose
        private String customerCity;
        @SerializedName("MoveInCityYear")
        @Expose
        private String moveInCityYear;
        @SerializedName("MoveInResidenceYear")
        @Expose
        private String moveInResidenceYear;
        @SerializedName("ResidenceType")
        @Expose
        private String residenceType;
        @SerializedName("AddressLine1")
        @Expose
        private String addressLine1;
        @SerializedName("AddressLine2")
        @Expose
        private String addressLine2;
        @SerializedName("AddressLine3")
        @Expose
        private String addressLine3;

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getCustomerCity() {
            return customerCity;
        }

        public void setCustomerCity(String customerCity) {
            this.customerCity = customerCity;
        }

        public String getMoveInCityYear() {
            return moveInCityYear;
        }

        public void setMoveInCityYear(String moveInCityYear) {
            this.moveInCityYear = moveInCityYear;
        }

        public String getMoveInResidenceYear() {
            return moveInResidenceYear;
        }

        public void setMoveInResidenceYear(String moveInResidenceYear) {
            this.moveInResidenceYear = moveInResidenceYear;
        }

        public String getResidenceType() {
            return residenceType;
        }

        public void setResidenceType(String residenceType) {
            this.residenceType = residenceType;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getAddressLine3() {
            return addressLine3;
        }

        public void setAddressLine3(String addressLine3) {
            this.addressLine3 = addressLine3;
        }

    }

