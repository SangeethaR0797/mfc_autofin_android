package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountAndCustData
{

        @SerializedName("count")
        @Expose
        private Count count;
        @SerializedName("customers")
        @Expose
        private List<CustomerData> customers = null;

        public Count getCount() {
            return count;
        }

        public void setCount(Count count) {
            this.count = count;
        }

        public List<CustomerData> getCustomers() {
            return customers;
        }

        public void setCustomers(List<CustomerData> customers) {
            this.customers = customers;
        }

    }

