package model.add_lead_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerID
{

        @SerializedName("CustomerId")
        @Expose
        private Integer customerId;

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

}
