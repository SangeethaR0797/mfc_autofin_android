package v2.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomLoanProcessCompletedData(
        public var customerId:Int=0,
        public var customerName: String = "",
        public var caseID: String = ""
) : Parcelable