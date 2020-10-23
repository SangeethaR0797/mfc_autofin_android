package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CustomerDetailsRes
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
        private List<CustomerData> data = null;

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

        public List<CustomerData> getData() {
            return data;
        }

        public void setData(List<CustomerData> data) {
            this.data = data;
        }

    }

