package v2.model.response

import com.google.gson.annotations.SerializedName


data class AddLeadResponse(
        @SerializedName("data")
        var mData: Int?,
        @SerializedName("message")
        var message: String?,
        @SerializedName("status")
        var SerializedName: Boolean?,
        @SerializedName("statusCode")
        var statusCode: String?
)