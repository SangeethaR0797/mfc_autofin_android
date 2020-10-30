package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import model.vehicle_details.vehicle_category.VehData;

public class VehicleCategoryRes
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
        private VehData data;

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

        public VehData getData() {
            return data;
        }

        public void setData(VehData data) {
            this.data = data;
        }

    }

