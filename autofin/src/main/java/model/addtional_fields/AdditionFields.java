package model.addtional_fields;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AdditionFields
{

    @SerializedName("bankId")
    @Expose
    private String bankId;
    @SerializedName("fieldName")
    @Expose
    private String fieldName;
    @SerializedName("fieldType")
    @Expose
    private String fieldType;
    @SerializedName("apiKeyName")
    @Expose
    private String apiKeyName;
    @SerializedName("isMandatory")
    @Expose
    private Boolean isMandatory;
    @SerializedName("isMasterDriven")
    @Expose
    private Boolean isMasterDriven;
    @SerializedName("data")
    @Expose
    private List<AdditionalFieldData> data = null;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getApiKeyName() {
        return apiKeyName;
    }

    public void setApiKeyName(String apiKeyName) {
        this.apiKeyName = apiKeyName;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Boolean getIsMasterDriven() {
        return isMasterDriven;
    }

    public void setIsMasterDriven(Boolean isMasterDriven) {
        this.isMasterDriven = isMasterDriven;
    }

    public List<AdditionalFieldData> getData() {
        return data;
    }

    public void setData(List<AdditionalFieldData> data) {
        this.data = data;
    }

}
