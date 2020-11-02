package model.otp_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPResponse
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
        private String data;

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

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

}