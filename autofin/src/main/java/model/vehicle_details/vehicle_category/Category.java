package model.vehicle_details.vehicle_category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category
{


        @SerializedName("displayLabel")
        @Expose
        private String displayLabel;
        @SerializedName("value")
        @Expose
        private String value;

        public String getDisplayLabel() {
            return displayLabel;
        }

        public void setDisplayLabel(String displayLabel) {
            this.displayLabel = displayLabel;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }


