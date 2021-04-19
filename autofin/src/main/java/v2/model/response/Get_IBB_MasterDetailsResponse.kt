package v2.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Get_IBB_MasterDetailsResponse(
        @SerializedName("status")
        @Expose
        private var status: String? = null,

        @SerializedName("message")
        @Expose
        private var message: String? = null,

        @SerializedName("year")
        @Expose
        private var year: List<String> = listOf(),

        @SerializedName("month")
        @Expose
        private var month: List<String> = listOf(),

        @SerializedName("make")
        @Expose
        private var make: List<String> = listOf(),

        @SerializedName("model")
        @Expose
        private var model: List<String> = listOf(),

        @SerializedName("variant")
        @Expose
        private var variant: List<String> = listOf()


) : Parcelable
