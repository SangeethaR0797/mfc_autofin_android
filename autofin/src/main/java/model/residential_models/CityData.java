package model.residential_models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityData
{


        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

}
