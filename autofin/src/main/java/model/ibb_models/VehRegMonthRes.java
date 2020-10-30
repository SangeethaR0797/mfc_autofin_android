package model.ibb_models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehRegMonthRes
{


        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("month")
        @Expose
        private List<String> month = null;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<String> getMonth() {
            return month;
        }

        public void setMonth(List<String> month) {
            this.month = month;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

