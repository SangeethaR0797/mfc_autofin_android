package v2.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Get_IBB_MasterDetailsRequest(
        @SerializedName("for")
        @Expose
        private var mfor: String? = null,

        @SerializedName("access_token")
        @Expose
        private var access_token: String? = null,

        @SerializedName("pricefor")
        @Expose
        private var pricefor: String? = null,

        @SerializedName("tag")
        @Expose
        private var tag: String? = null,

        @SerializedName("year")
        @Expose
        private var year: String? = null,

        @SerializedName("month")
        @Expose
        private var month: String? = null,

        @SerializedName("make")
        @Expose
        private var make: String? = null,

        @SerializedName("model")
        @Expose
        private var model: String? = null


) : Parcelable
