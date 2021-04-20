package v2.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
public class VehicleAddUpdateDTO(
        public var transactionId: String? = null,
        public var registrationNumber: String? = null,
        public var year: String? = null,
        public var make: String? = null,
        public var model: String? = null,
        public var variant: String? = null,
        public var ownership: String? = null,
        public var kilometres_driven: String? = null,
        public var fule_type: String? = null,
        public var price: String? = null
) : Parcelable