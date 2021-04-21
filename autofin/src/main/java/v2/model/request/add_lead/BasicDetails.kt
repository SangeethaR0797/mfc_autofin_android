package v2.model.request.add_lead

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
public class BasicDetails(

        @SerializedName("FirstName") public var FirstName: String? = null,
        @SerializedName("LastName") public var LastName: String? = null,
        @SerializedName("Email") public var Email: String? = null,
        @SerializedName("CustomerMobile") public var CustomerMobile: String? = null,
        @SerializedName("Salutation") public var Salutation: String? = null

) : Parcelable