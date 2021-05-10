package v2.model.request.add_lead

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
public data class AddLeadData(
        @SerializedName("basicDetails") public var basicDetails: BasicDetails? = null,
        @SerializedName("vehicleDetails") public var addLeadVehicleDetails: AddLeadVehicleDetails? = null
) : Parcelable
