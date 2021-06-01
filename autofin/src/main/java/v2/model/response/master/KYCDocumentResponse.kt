package v2.model.response.master

import com.google.gson.annotations.SerializedName

data class KYCDocumentResponse
(
        @SerializedName("status") var status : Boolean,
        @SerializedName("message") var message : String,
        @SerializedName("statusCode") var statusCode : String,
        @SerializedName("data") var data : KYCDocumentData

)

data class KYCDocumentData (

        @SerializedName("nonGroupedDoc") var nonGroupedDoc : List<NonGroupedDoc>,
        @SerializedName("groupedDoc") var groupedDoc : List<GroupedDoc>

)

data class Docs (

        @SerializedName("apiKey") var apiKey : String,
        @SerializedName("description") var description : String,
        @SerializedName("displayLabel") var displayLabel : String

)

data class GroupedDoc (

        @SerializedName("groupName") var groupName : String,
        @SerializedName("description") var description : String,
        @SerializedName("docs") var docs : List<Docs>

)

data class NonGroupedDoc (

        @SerializedName("apiKey") var apiKey : String,
        @SerializedName("description") var description : String,
        @SerializedName("displayLabel") var displayLabel : String

)