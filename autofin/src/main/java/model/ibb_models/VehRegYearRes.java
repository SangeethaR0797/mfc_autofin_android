package model.ibb_models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehRegYearRes
{
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("year")
        @Expose
        private List<String> year = null;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<String> getYear() {
            return year;
        }

        public void setYear(List<String> year) {
            this.year = year;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

