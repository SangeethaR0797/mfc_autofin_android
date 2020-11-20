package model.kyc_model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doc
{
     @SerializedName("Key")
        @Expose
        private String key;
        @SerializedName("Url")
        @Expose
        private String url;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

