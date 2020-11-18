package model.kyc_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadDocRequest
{

        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("UserType")
        @Expose
        private String userType;
        @SerializedName("Data")
        @Expose
        private DocumentData data;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public DocumentData getData() {
            return data;
        }

        public void setData(DocumentData data) {
            this.data = data;
        }

}
