package v2.model.response.master

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EquifaxData(
        var address: List<Addres>? = null,
        var birthDate: List<String>? = null,
        var panNumber: List<String>? = null
) : Parcelable
@Parcelize
data class Addres(
        var address: String? = null,
        var pincode: String? = null
) : Parcelable