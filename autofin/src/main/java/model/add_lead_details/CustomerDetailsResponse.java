package model.add_lead_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetailsResponse
{

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private Object message;
        @SerializedName("statusCode")
        @Expose
        private Object statusCode;
        @SerializedName("data")
        @Expose
        private CustomerDetails data;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public Object getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Object statusCode) {
            this.statusCode = statusCode;
        }

        public CustomerDetails getData() {
            return data;
        }

        public void setData(CustomerDetails data) {
            this.data = data;
        }

    }

