package model.addtional_fields;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SubmitAdditionalFieldData
{

    @SerializedName("CustomerId")
    @Expose
    private Integer customerId;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("Fields")
    @Expose
    private List<Field> fields = null;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
