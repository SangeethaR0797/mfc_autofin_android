package v2.model.request.add_lead

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
public data class IBBPriceRequest(
        @SerializedName("UserId") var UserId : String,
        @SerializedName("UserType") var UserType : String,
        @SerializedName("Data") var Data : IBBPriceData
):Parcelable

@Parcelize
data class IBBPriceData(

        @SerializedName("year") var year : String,
        @SerializedName("make") var make : String,
        @SerializedName("model") var model : String,
        @SerializedName("variant") var variant : String,
        @SerializedName("kilometer") var kilometer : String,
        @SerializedName("owner") var owner : String,
        @SerializedName("vehicleNumber") var vehicleNumber : String

) : Parcelable