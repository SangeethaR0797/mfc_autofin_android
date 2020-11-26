package model.basic_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SalutationData
{

        @SerializedName("types")
        @Expose
        private List<SalutationType> types = null;

        public List<SalutationType> getTypes() {
            return types;
        }

        public void setTypes(List<SalutationType> types) {
            this.types = types;
        }

}
