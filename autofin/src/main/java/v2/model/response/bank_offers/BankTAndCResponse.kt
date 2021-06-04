package v2.model.response.bank_offers

import com.google.gson.annotations.SerializedName

data class BankTAndCResponse
(
        @SerializedName("status") var status : Boolean,
        @SerializedName("message") var message : String,
        @SerializedName("statusCode") var statusCode : String,
        @SerializedName("data") var data : TAndCData

)

data class TAndCData (

        @SerializedName("termsAndCondition") var termsAndCondition : String,
        @SerializedName("privacyPolicy") var privacyPolicy : String

)


