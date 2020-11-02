package model.otp_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerMobile {

        @SerializedName("CustomerMobile")
        @Expose
        private String customerMobile;

        public String getCustomerMobile() {
            return customerMobile;
        }

        public void setCustomerMobile(String customerMobile) {
            this.customerMobile = customerMobile;
        }

    }

