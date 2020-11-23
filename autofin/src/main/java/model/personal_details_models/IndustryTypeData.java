package model.personal_details_models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class IndustryTypeData {

        @SerializedName("types")
        @Expose
        private List<IndustryType> types = null;

        public List<IndustryType> getTypes() {
            return types;
        }

        public void setTypes(List<IndustryType> types) {
            this.types = types;
        }

}

