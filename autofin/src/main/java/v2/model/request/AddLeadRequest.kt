package v2.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import v2.model.request.add_lead.AddLeadData

@Parcelize
public data class AddLeadRequest(

        @SerializedName("UserId") public var UserId: String? = null,
        @SerializedName("UserType") public var UserType: String? = null,
        @SerializedName("Data") public var Data: AddLeadData? = null


) : Parcelable




