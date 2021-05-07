package v2.model.request.bank_api_request

import com.google.gson.annotations.SerializedName

data class RecommendedBankRequestData
(

    @SerializedName("CustomerId") var CustomerId : String="",
    @SerializedName("ReqTenure") var ReqTenure : Int=0,
    @SerializedName("LoanAmount") var LoanAmount : Int=0,
    @SerializedName("CaseId") var CaseId : String=""

)