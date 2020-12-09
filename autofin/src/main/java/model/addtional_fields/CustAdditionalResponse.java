package model.addtional_fields;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustAdditionalResponse
{

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private Object message;
        @SerializedName("statusCode")
        @Expose
        private String statusCode;
        @SerializedName("data")
        @Expose
        private List<CustAdditionalData> data = null;

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

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public List<CustAdditionalData> getData() {
            return data;
        }

        public void setData(List<CustAdditionalData> data) {
            this.data = data;
        }

}
