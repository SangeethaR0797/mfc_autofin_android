package v2.model.response.master

import com.google.gson.annotations.SerializedName

public data class PinCodeResponse
(
        @SerializedName("status") var status : Boolean,
        @SerializedName("message") var message : String,
        @SerializedName("statusCode") var statusCode : String,
        @SerializedName("data") var data : PinCodeData

)
data class PinCodeData (

        @SerializedName("pincode") var pincode : String,
        @SerializedName("city") var city : String,
        @SerializedName("state") var state : String

)