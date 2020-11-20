package model.kyc_model;

import java.util.List;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import model.add_lead_details.Document;

public class DocumentData
{
        @SerializedName("CustomerId")
        @Expose
        private Integer customerId;
        @SerializedName("CaseId")
        @Expose
        private String caseId;
        @SerializedName("Docs")
        @Expose
        private List<Doc> docs = null;

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public List<Doc> getDocs() {
            return docs;
        }

        public void setDocs(List<Doc> docs) {
            this.docs = docs;
        }

}
