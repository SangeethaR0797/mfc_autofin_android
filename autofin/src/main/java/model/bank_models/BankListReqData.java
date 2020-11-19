package model.bank_models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankListReqData
{
        @SerializedName("CustomerId")
        @Expose
        private String customerId;
        @SerializedName("CaseId")
        @Expose
        private String caseId;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }
}
