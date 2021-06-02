package v2.model.response.master

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class KYCDocumentResponse
(
        @SerializedName("status") var status: Boolean,
        @SerializedName("message") var message: String,
        @SerializedName("statusCode") var statusCode: String,
        @SerializedName("data") var data: KYCDocumentData

) : Parcelable

@Parcelize
data class KYCDocumentData(

        @SerializedName("nonGroupedDoc") var nonGroupedDoc: List<NonGroupedDoc>,
        @SerializedName("groupedDoc") var groupedDoc: List<GroupedDoc>

) : Parcelable

@Parcelize
data class Docs(

        @SerializedName("apiKey") var apiKey: String,
        @SerializedName("description") var description: String,
        @SerializedName("displayLabel") var displayLabel: String

) : Parcelable

@Parcelize
data class GroupedDoc(

        @SerializedName("groupName") var groupName: String,
        @SerializedName("description") var description: String,
        @SerializedName("docs") var docs: List<Docs>

) : Parcelable

@Parcelize
data class NonGroupedDoc(

        @SerializedName("apiKey") var apiKey: String,
        @SerializedName("description") var description: String,
        @SerializedName("displayLabel") var displayLabel: String

) : Parcelable