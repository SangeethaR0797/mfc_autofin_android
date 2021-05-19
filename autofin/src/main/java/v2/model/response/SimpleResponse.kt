package v2.model.response

import com.google.gson.annotations.SerializedName

data class SimpleResponse (
        @SerializedName("status") var status : Boolean,
        @SerializedName("message") var message : String,
        @SerializedName("statusCode") var statusCode : String,
        @SerializedName("data") var data : Int

)
