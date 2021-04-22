package v2.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
public data class StockDetails (

    @SerializedName("stockId")
    var stockId: String = "",
    @SerializedName("make")
    var make: String = "",
    @SerializedName("model")
    var model: String = "",
    @SerializedName("variant")
    var variant: String = "",
    @SerializedName("fuelType")
    var fuelType: String = "",
    @SerializedName("insurance")
    var insurance: String = "",
    @SerializedName("insuranceType")
    var insuranceType: String = "",
    @SerializedName("insuranceValidity")
    var insuranceValidity: String = "",
    @SerializedName("year")
    var year: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("city")
    var city: String = "",
    @SerializedName("ibbMake")
    var ibbMake: String = "",
    @SerializedName("ibbModel")
    var ibbModel: String = "",
    @SerializedName("ibbVariant")
    var ibbVariant: String = "",
    @SerializedName("owner")
    var owner: String = "",
    @SerializedName("kMs")
    var kMs: String = "",
    @SerializedName("registrationNumber")
    var registrationNumber: String = "",
    @SerializedName("vehicleSellingPrice")
    var vehicleSellingPrice: String = ""

): Parcelable