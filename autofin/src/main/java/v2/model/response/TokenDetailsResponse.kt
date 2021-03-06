package v2.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TokenDetailsResponse(
    @Expose
    @SerializedName("status")
    var status: Boolean,
    @Expose
    @SerializedName("message")
    var message: String,
    @Expose
    @SerializedName("statusCode")
    var statusCode: String,
    @Expose
    @SerializedName("data")
    var data: TokenDetails
) : Parcelable

@Parcelize
data class TokenDetails(

    @Expose
    @SerializedName("token")
    var token: String,
    @Expose
    @SerializedName("expiresOn")
    var expiresOn: String,

    ) : Parcelable