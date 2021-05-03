package v2.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataSelectionDTO(
        public var displayValue: String? = null,
        public var displayValuePostFix: String? = null,
        public var value: String? = null,
        public var selected: Boolean,
        public var imageUrl: String? = null
) : Parcelable