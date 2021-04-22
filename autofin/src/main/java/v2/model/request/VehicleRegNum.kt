package v2.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VehicleRegNum {
    @SerializedName("vehicleNumber")
    @Expose
    public var vehicleNumber: String? = ""

}