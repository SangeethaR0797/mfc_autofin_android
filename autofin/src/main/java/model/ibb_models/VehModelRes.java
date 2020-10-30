package model.ibb_models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehModelRes
{

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("model")
        @Expose
        private List<String> model = null;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<String> getModel() {
            return model;
        }

        public void setModel(List<String> model) {
            this.model = model;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

}
