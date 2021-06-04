package v2.model.request

import com.google.gson.annotations.SerializedName

data class ValidateFinalOTPRequest
(
        @SerializedName("UserId") var UserId: String,
        @SerializedName("UserType") var UserType: String,
        @SerializedName("Data") var Data: ValidateOTPData
)

data class ValidateOTPData (

        @SerializedName("CustomerId") var CustomerId : Int,
        @SerializedName("OTP") var OTP : String

)