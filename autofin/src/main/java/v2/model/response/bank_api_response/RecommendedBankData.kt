package v2.model.response.bank_api_response

import com.google.gson.annotations.SerializedName

data class RecommendedBankData(
        @SerializedName("bankId") var bankId : Int,
        @SerializedName("bankName") var bankName : String,
        @SerializedName("loanAmount") var loanAmount : String,
        @SerializedName("roi") var roi : String,
        @SerializedName("emi") var emi : String,
        @SerializedName("tenure") var tenure : String,
        @SerializedName("message") var message : String

)