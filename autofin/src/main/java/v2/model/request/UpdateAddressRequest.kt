package v2.model.request

import com.google.gson.annotations.SerializedName

public data class UpdateAddressRequest
(
        @SerializedName("UserId") var UserId: String,
        @SerializedName("UserType") var UserType: String,
        @SerializedName("Data") var Data: AddressData
)

data class AddressData(
        @SerializedName("CustomerId") var CustomerId: Int,
        @SerializedName("currentAddress") var currentAddress: CurrentAddress,
        @SerializedName("permanentAddress") var permanentAddress: PermanentAddress
)

data class CurrentAddress(
        @SerializedName("IsPermanent") var IsPermanent: Boolean,
        @SerializedName("Pincode") var Pincode: String,
        @SerializedName("Address") var Address: String)

data class PermanentAddress(
        @SerializedName("Pincode") var Pincode: String,
        @SerializedName("Address") var Address: String
)