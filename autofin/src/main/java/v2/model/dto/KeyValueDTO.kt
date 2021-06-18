package v2.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeyValueDTO(
    public var key: String? = null,
    public var value: String? = null,
) : Parcelable