package v2.model.response.master

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
public data class IBBPriceResponse(@SerializedName("status") var status : Boolean,
                                   @SerializedName("message") var message : String,
                                   @SerializedName("statusCode") var statusCode : String,
                                   @SerializedName("data") var data : Int
):Parcelable
