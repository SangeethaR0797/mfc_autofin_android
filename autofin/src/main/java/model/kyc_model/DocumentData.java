package model.kyc_model;

import java.util.List;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentData
{
        @SerializedName("CustomerId")
        @Expose
        private Integer customerId;
        @SerializedName("CaseId")
        @Expose
        private String caseId;
        @SerializedName("Key")
        @Expose
        private String key;
        @SerializedName("Docs")
        @Expose
        private List<String> docs = null;

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

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getDocs() {
            return docs;
        }

        public void setDocs(List<String> docs) {
            this.docs = docs;
        }

}
