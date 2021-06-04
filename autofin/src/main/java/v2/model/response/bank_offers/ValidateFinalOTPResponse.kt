package v2.model.response.bank_offers

import com.google.gson.annotations.SerializedName

data class ValidateFinalOTPResponse
(
        @SerializedName("status") var status : Boolean,
        @SerializedName("message") var message : String,
        @SerializedName("statusCode") var statusCode : String,
        @SerializedName("data") var data : String


)