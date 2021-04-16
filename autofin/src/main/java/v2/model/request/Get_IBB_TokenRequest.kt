package v2.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Get_IBB_TokenRequest(
        @SerializedName("password")
        @Expose
        private var password: String? = null,

        @SerializedName("username")
        @Expose
        private var username: String? = null
) : Parcelable