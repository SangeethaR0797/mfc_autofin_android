package model.otp_models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPRequest
{

        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("UserType")
        @Expose
        private String userType;
        @SerializedName("Data")
        @Expose
        private CustomerMobile data;

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

        public CustomerMobile getData() {
            return data;
        }

        public void setData(CustomerMobile data) {
            this.data = data;
        }

}
