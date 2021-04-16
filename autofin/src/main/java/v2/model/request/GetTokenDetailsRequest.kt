package v2.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class GetTokenDetailsRequest(
        @SerializedName("UserId")
        @Expose
        private var userId: String? = null,

        @SerializedName("UserType")
        @Expose
        private var userType: String? = null,

        @SerializedName("RequestFrom")
        @Expose
        private var requestFrom: String? = null,

        @SerializedName("Data")
        @Expose
        private var data: String? = null


) : Parcelable