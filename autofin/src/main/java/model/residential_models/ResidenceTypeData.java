package model.residential_models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResidenceTypeData {


    @SerializedName("types")
    @Expose
    private List<ResidenceType> types = null;

    public List<ResidenceType> getTypes() {
        return types;
    }

    public void setTypes(List<ResidenceType> types) {
        this.types = types;
    }

}
