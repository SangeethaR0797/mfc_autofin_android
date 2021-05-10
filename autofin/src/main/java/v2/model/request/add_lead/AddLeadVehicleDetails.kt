package v2.model.request.add_lead

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddLeadVehicleDetails(

        @SerializedName("RegistrationYear") public var RegistrationYear: Int? = null,
        @SerializedName("Make") public var Make: String? = null,
        @SerializedName("Variant") public var Variant: String? = null,
        @SerializedName("Model") public var Model: String? = null,
        @SerializedName("Ownership") public var Ownership: Int? = null,
        @SerializedName("VehicleNumber") public var VehicleNumber: String? = null,
        @SerializedName("VehicleSellingPrice") public var VehicleSellingPrice: String? = null,
        @SerializedName("KMs") public var KMs: String? = null,
        @SerializedName("FuelType") public var FuelType: String? = null

) : Parcelable