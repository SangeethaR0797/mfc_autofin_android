package v2.model.response

import com.google.gson.annotations.SerializedName

data class SubmitAdditionalFieldRequest
(
        @SerializedName("UserId") var UserId : String,
        @SerializedName("UserType") var UserType : String,
        @SerializedName("Data") var Data : FieldData

)

data class FieldData (
        @SerializedName("CustomerId") var CustomerId : Int,
        @SerializedName("details") var details : List<FieldDetails>
)

data class FieldDetails (
        @SerializedName("APIKey") var APIKey : String,
        @SerializedName("Value") var Value : String,
        @SerializedName("DisplayLabel") var DisplayLabel : String
)