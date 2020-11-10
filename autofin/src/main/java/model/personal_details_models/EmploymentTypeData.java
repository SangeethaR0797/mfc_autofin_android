package model.personal_details_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmploymentTypeData
{
    @SerializedName("types")
    @Expose
    private List<EmpTypeList> types = null;

    public List<EmpTypeList> getTypes() {
        return types;
    }

    public void setTypes(List<EmpTypeList> types) {
        this.types = types;
    }
}
