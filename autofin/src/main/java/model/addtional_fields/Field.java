package model.addtional_fields;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Field {

        @SerializedName("Key")
        @Expose
        private String key;
        @SerializedName("Value")
        @Expose
        private String value;

    public Field(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
}
