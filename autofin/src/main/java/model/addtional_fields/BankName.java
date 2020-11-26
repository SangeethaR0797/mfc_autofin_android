package model.addtional_fields;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankName
{

        @SerializedName("BankName")
        @Expose
        private String bankName;

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

}
