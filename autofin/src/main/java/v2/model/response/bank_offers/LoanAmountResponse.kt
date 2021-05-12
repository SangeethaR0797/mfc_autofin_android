package v2.model.response.bank_offers

import com.google.gson.annotations.SerializedName

data class LoanAmountResponse
(

    @SerializedName("status") var status : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("statusCode") var statusCode : String,
    @SerializedName("data") var data : LoanData

)

data class LoanData (

        @SerializedName("minValues") var minValues : MinValues,
        @SerializedName("maxValues") var maxValues : MaxValues,
        @SerializedName("defaultValues") var defaultValues : DefaultValues

)

data class MinValues (

        @SerializedName("loanAmount") var loanAmount : Int,
        @SerializedName("tenureInMonths") var tenureInMonths : Int

)

data class MaxValues (

        @SerializedName("loanAmount") var loanAmount : Int,
        @SerializedName("tenureInMonths") var tenureInMonths : Int

)
data class DefaultValues (

        @SerializedName("loanAmount") var loanAmount : Int,
        @SerializedName("tenureInMonths") var tenureInMonths : Int

)