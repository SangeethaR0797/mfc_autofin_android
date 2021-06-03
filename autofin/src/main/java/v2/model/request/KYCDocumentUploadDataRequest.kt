package v2.model.request

import com.google.gson.annotations.SerializedName

data class KYCDocumentUploadDataRequest(
        @SerializedName("UserId") var UserId : String,
        @SerializedName("UserType") var UserType : String,
        @SerializedName("Data") var Data : KYCUploadDocumentData
)

data class KYCUploadDocumentData (
        @SerializedName("CustomerId") var CustomerId : Int,
        @SerializedName("CaseId") var CaseId : String,
        @SerializedName("Docs") var Docs : List<KYCUploadDocs>
)

data class KYCUploadDocs (
        @SerializedName("Key") var Key : String,
        @SerializedName("Url") var Url : String
)