package v2.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StockResponse
(
    @SerializedName("status") var status : Boolean=false,
    @SerializedName("message") var message : String="",
    @SerializedName("statusCode") var statusCode : String="",
    @SerializedName("data") var data : StockDetails? = StockDetails()

    ): Parcelable