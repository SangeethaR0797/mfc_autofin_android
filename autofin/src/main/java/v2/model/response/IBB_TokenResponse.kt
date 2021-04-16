package v2.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class IBB_TokenResponse(
        @Expose
        @SerializedName("status")
        var status: Boolean,
        @Expose
        @SerializedName("access_token")
        var access_token: String,
        @Expose
        @SerializedName("expires_at")
        var expires_at: String
) : Parcelable