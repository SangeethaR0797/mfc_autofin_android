package model.personal_details_models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndustryTypeRes
{
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("statusCode")
        @Expose
        private String statusCode;
        @SerializedName("data")
        @Expose
        private IndustryTypeData data;

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

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public IndustryTypeData getData() {
            return data;
        }

        public void setData(IndustryTypeData data) {
            this.data = data;
        }

    }
