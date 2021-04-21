package v2.model.request.add_lead

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
public class AddLeadData(
        @SerializedName("basicDetails") public var basicDetails: BasicDetails? = null,
        @SerializedName("vehicleDetails") public var vehicleDetails: VehicleDetails? = null
) : Parcelable
