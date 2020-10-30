package model.ibb_models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessTokenRes
{


        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("access_token")
        @Expose
        private String accessToken;
        @SerializedName("expires_at")
        @Expose
        private String expiresAt;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
        }

    }

