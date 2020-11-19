package model.add_lead_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Document {

        @SerializedName("keyName")
        @Expose
        private String keyName;
        @SerializedName("docPath")
        @Expose
        private List<String> docPath = null;
        @SerializedName("subKeyName")
        @Expose
        private List<String> subKeyName = null;

        public String getKeyName() {
            return keyName;
        }

        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        public List<String> getDocPath() {
            return docPath;
        }

        public void setDocPath(List<String> docPath) {
            this.docPath = docPath;
        }

        public List<String> getSubKeyName() {
            return subKeyName;
        }

        public void setSubKeyName(List<String> subKeyName) {
            this.subKeyName = subKeyName;
        }

}
