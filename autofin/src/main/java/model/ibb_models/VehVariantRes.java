package model.ibb_models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class VehVariantRes
{
        @SerializedName("status")
        @Expose
        private int status;
        @SerializedName("variant")
        @Expose
        private List<String> variant = null;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getStatus() {
        return status;
    }

        public void setStatus(Integer status) {
        this.status = status;
    }

        public List<String> getVariant() {
        return variant;
    }

        public void setVariant(List<String> variant) {
        this.variant = variant;
    }

        public String getMessage() {
        return message;
    }

        public void setMessage(String message) {
        this.message = message;
    }

    }
