package v2.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import v2.model.request.add_lead.AddLeadData

public class StockDetailsReq {

    @SerializedName("userId")
    public var UserId: String? = null
    @SerializedName("userType")
    public var UserType: String? = null
    @SerializedName("requestFrom")
    public var RequestFrom: String? = null
    @SerializedName("data")
    var data : VehicleRegNum? = null
    @SerializedName("vehicleNumber")
    public var vehicleNumberE : String? =null
    @SerializedName("accessKey")
    public var accessKey : String? =null

}