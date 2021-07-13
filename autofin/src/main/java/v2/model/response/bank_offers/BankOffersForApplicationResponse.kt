package v2.model.response.bank_offers

import com.google.gson.annotations.SerializedName

data class BankOffersForApplicationResponse(
    var data: List<BankOffersData>?,
    var message: Any?,
    var status: Boolean?,
    var statusCode: String?
)

data class BankOffersData(
        @SerializedName("bankId") var bankId : Int,
        @SerializedName("bankName") var bankName : String,
        @SerializedName("loanAmount") var loanAmount : String,
        @SerializedName("roi") var roi : String,
        @SerializedName("emi") var emi : String,
        @SerializedName("tenure") var tenure : String,
        @SerializedName("message") var message : String,
        @SerializedName("isRecommended") var isRecommended : Boolean,
        @SerializedName("bankLogoUrl") var bankLogoUrl : String,
        @SerializedName("processingFees") var processingFees : String

)