package v2.model.request.bank_api_request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
public class RecommendedBankRequest(

        @SerializedName("UserId") public var UserId: String? = null,
        @SerializedName("UserType") public var UserType: String? = null,
        @SerializedName("Data") public var Data: RecommendedBankRequestData? = null


) : Parcelable