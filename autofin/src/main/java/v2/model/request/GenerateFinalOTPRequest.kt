package v2.model.request

import com.google.gson.annotations.SerializedName

data class GenerateFinalOTPRequest (
        @SerializedName("UserId") var UserId : String,
        @SerializedName("UserType") var UserType : String,
        @SerializedName("Data") var Data : FinalOTPData

)

data class FinalOTPData (

        @SerializedName("CustomerId") var CustomerId : Int,
        @SerializedName("IsBankTandCChecked") var IsBankTandCChecked : Boolean

)
