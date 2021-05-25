package v2.model.response.master

import com.google.gson.annotations.SerializedName

data class APIDropDownResponse
(

        @SerializedName("status") var status: Boolean,
        @SerializedName("message") var message: String,
        @SerializedName("statusCode") var statusCode: String,
        @SerializedName("data") var data: APIDropDownData

)

data class APIDropDownData
(
        @SerializedName("key") var key: String,
        @SerializedName("details") var details: List<Details>

)

data class Details(

        @SerializedName("displayLabel") var displayLabel: String,
        @SerializedName("value") var value: String

)


