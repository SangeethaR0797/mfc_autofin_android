package v2.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataSelectionDTO(
        var displayValue: String? = null,
        var selected: Boolean
) : Parcelable