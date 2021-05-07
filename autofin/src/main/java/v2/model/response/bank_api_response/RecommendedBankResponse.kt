package v2.model.response.bank_api_response

import com.google.gson.annotations.SerializedName

data class RecommendedBankResponse(

    @SerializedName("status") var status : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("statusCode") var statusCode : String,
    @SerializedName("data") var data : List<RecommendedBankData>
)
