package model.ibb_models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehMakeRes
{

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("make")
        @Expose
        private List<String> make = null;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<String> getMake() {
            return make;
        }

        public void setMake(List<String> make) {
            this.make = make;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

