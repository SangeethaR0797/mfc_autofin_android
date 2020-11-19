package model.basic_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicDetailsResponse
{

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("statusCode")
        @Expose
        private Object statusCode;
        @SerializedName("data")
        @Expose
        private Integer data;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Object statusCode) {
            this.statusCode = statusCode;
        }

        public Integer getData() {
            return data;
        }

        public void setData(Integer data) {
            this.data = data;
        }

}
