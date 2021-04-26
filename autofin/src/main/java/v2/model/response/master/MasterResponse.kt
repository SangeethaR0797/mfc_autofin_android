package v2.model.response.master

import com.google.gson.annotations.SerializedName


data class MasterResponse(
        @SerializedName("status") var status: Boolean,
        @SerializedName("message") var message: String,
        @SerializedName("statusCode") var statusCode: String,
        @SerializedName("data") var data: MasterData
)

data class MasterData(
        @SerializedName("types") var types: List<Types>
)

data class Types(
        @SerializedName("displayLabel") var displayLabel: String,
        @SerializedName("value") var value: String
)