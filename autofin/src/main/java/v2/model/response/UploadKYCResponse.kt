package v2.model.response

import com.google.gson.annotations.SerializedName

data class UploadKYCResponse
(
        @SerializedName("status") var status : Boolean,
        @SerializedName("message") var message : String,
        @SerializedName("statusCode") var statusCode : String,
        @SerializedName("data") var data : List<String>

)