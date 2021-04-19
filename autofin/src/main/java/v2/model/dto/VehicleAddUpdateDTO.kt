package v2.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class VehicleAddUpdateDTO(
        var registrationNumber: String? = null,
        var year: String,
        var make: String? = null,
        var model: String? = null,
        var variant: String? = null,
        var ownership: String? = null,
        var kilometres_driven: String? = null,
        var fule_type: String? = null
) : Parcelable