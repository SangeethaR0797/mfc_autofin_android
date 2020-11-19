package model.basic_details;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicVehDetails {

        @SerializedName("LikelyPurchaseDate")
        @Expose
        private String likelyPurchaseDate;

        public String getLikelyPurchaseDate() {
            return likelyPurchaseDate;
        }

        public void setLikelyPurchaseDate(String likelyPurchaseDate) {
            this.likelyPurchaseDate = likelyPurchaseDate;
        }

}
