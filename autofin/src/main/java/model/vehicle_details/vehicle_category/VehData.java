package model.vehicle_details.vehicle_category;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class VehData
{

        @SerializedName("categories")
        @Expose
        private List<Category> categories = null;

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

    }


