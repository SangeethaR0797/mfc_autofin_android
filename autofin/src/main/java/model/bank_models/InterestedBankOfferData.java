package model.bank_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterestedBankOfferData {


        @SerializedName("CaseId")
        @Expose
        private String caseId;

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

}
