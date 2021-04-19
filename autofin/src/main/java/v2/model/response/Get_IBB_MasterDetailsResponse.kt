package v2.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Get_IBB_MasterDetailsResponse(
        @SerializedName("status")
        @Expose
         var status: String? = null,

        @SerializedName("message")
        @Expose
         var message: String? = null,

        @SerializedName("year")
        @Expose
         var year: List<String> = listOf(),

        @SerializedName("month")
        @Expose
         var month: List<String> = listOf(),

        @SerializedName("make")
        @Expose
         var make: List<String> = listOf(),

        @SerializedName("model")
        @Expose
         var model: List<String> = listOf(),

        @SerializedName("variant")
        @Expose
         var variant: List<String> = listOf()


) : Parcelable
